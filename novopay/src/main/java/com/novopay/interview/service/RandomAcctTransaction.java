package com.novopay.interview.service;

import com.novopay.interview.exception.LimitExhaustedException;
import com.novopay.interview.model.Customer;
import com.novopay.interview.model.TransactionDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service("randomAcctTransaction")
public class RandomAcctTransaction extends TransactionProcess {
    private final static Logger logger = LoggerFactory.getLogger(RandomAcctTransaction.class);

    private final static Queue<TransactionDetails> instructionList = new ConcurrentLinkedQueue<>();


    @Override
    public void initiateTransaction(TransactionDetails transactionDetails) {
        if (CollectionUtils.isEmpty(instructionList)) {
            logger.warn("No Transaction is present");
            return;
        }
        instructionList.forEach(transaction -> {
            try {
                amtTransaction(transaction.getFromAcct(), transaction.getToAcct(), transaction.getAmount());
            } catch (LimitExhaustedException | IOException e) {
                logger.info("Error Message {}", e.getMessage());
            }
        });

    }

    @Override
    public Queue<TransactionDetails> saveTransaction() {
        List<Long> accountNumbers = customerRecords.keySet()
                .stream()
                .collect(Collectors.toList());

        customerRecords.forEach((fromAcct, customer) -> {
            instructionList.add(new TransactionDetails(fromAcct
                    , getRandomAccountNo(fromAcct)
                    , getRendomIndex(5000, 10000).doubleValue()));
        });
        logger.info("instructionList {}", instructionList);
        return instructionList;
    }

    @Override
    public String getTransactionType() {
        return "RAT";
    }

    private long getRandomAccountNo(long fromAcctNo){
        Integer rendomIndex = getRendomIndex(0, customerRecords.size()-1);
        List<Long>accountNo = customerRecords.keySet().stream().collect(Collectors.toList());
        long toAcctNo =  accountNo.get(rendomIndex);
        if (fromAcctNo == toAcctNo){
            return  accountNo.get(rendomIndex+1);
        }
        return accountNo.get(rendomIndex);
    }

    private Integer getRendomIndex(int min, int max) {
        int idx = new Random().ints(min, max).findFirst().getAsInt();
        return idx;
    }
}
