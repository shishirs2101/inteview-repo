package com.novopay.interview.service;

import com.novopay.interview.dao.FileOperation;
import com.novopay.interview.exception.LimitExhaustedException;
import com.novopay.interview.model.Customer;
import com.novopay.interview.model.TransactionDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
public abstract class TransactionProcess implements Transactions {

    private final static Logger logger = LoggerFactory.getLogger(TransactionProcess.class);

    @Autowired
    FileOperation fileOperation;

    Map<Long, Customer> customerRecords;

    private final static List<TransactionDetails> transactionDetails = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        customerRecords = fileOperation.getCustomerRecords();
        if (CollectionUtils.isEmpty(customerRecords)){
            fileOperation.removeCustomerDetailsFile();
            logger.warn("No record has been found please add the records by calling addCustomer api");
        }
    }

    @Override
    public synchronized void amtTransaction(long fromAcct, long toAcct, double amount) throws LimitExhaustedException, IllegalArgumentException, IOException {
        if (null == customerRecords) init();
        if (!customerRecords.containsKey(fromAcct) || !customerRecords.containsKey(toAcct)){
            logger.warn("Either fromAcct {} or toAcct {} does not exit in customerRecords {}",fromAcct,toAcct,customerRecords.toString() );
            throw  new IllegalArgumentException("fromAcct/toAcct does not exits");
        }
        Customer fromAcctCustomer = customerRecords.get(fromAcct);
        Customer toAcctCustomer = customerRecords.get(toAcct);

        logger.info("Transaction:: between {} and {}",fromAcctCustomer,toAcctCustomer);
        if (amount > fromAcctCustomer.getGiftBalance()) {
            if (amount > fromAcctCustomer.getGiftCardBalance()) {
                double totalBalance = fromAcctCustomer.getGiftBalance()+fromAcctCustomer.getGiftCardBalance();
                String errorMessage = "AccountNumber "+fromAcct+" exhausted all limits. Balance left is "+totalBalance;
                fileOperation.writeLimitExhausted(errorMessage);
                logger.warn(errorMessage);
                throw new LimitExhaustedException(errorMessage);

            }else fromAcctCustomer.setGiftCardBalance(fromAcctCustomer.getGiftCardBalance()-amount);
        }else {
            fromAcctCustomer.setGiftBalance(fromAcctCustomer.getGiftBalance()-amount);
        }
        toAcctCustomer.setGiftBalance(toAcctCustomer.getGiftBalance()+amount);

        customerRecords.put(fromAcctCustomer.getAccountNumber(),fromAcctCustomer);
        customerRecords.put(toAcctCustomer.getAccountNumber(),toAcctCustomer);
        transactionDetails.add(new TransactionDetails(fromAcct,toAcct,amount));

        logger.info("Transaction Details:: {} \n Customer Records:: {}",transactionDetails,customerRecords);
        pintMapData();
    }

    @Override
    public Queue<TransactionDetails> saveTransaction() {
        return null;
    }

    private void pintMapData(){
        customerRecords.forEach((aLong, customer) -> {
            logger.info("( {} - {} )",aLong,customer.getGiftBalance()+customer.getGiftCardBalance());
        });
    }
}
