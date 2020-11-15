package com.novopay.interview.service;

import com.novopay.interview.exception.LimitExhaustedException;
import com.novopay.interview.model.TransactionDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("acctToAcctTransaction")
public class AcctToAcctTransaction extends TransactionProcess {

    private final static Logger logger = LoggerFactory.getLogger(AcctToAcctTransaction.class);


    @Override
    public void initiateTransaction(TransactionDetails transactionDetails) throws IOException {
        amtTransaction(transactionDetails.getFromAcct(),transactionDetails.getToAcct(),transactionDetails.getAmount());
    }

    @Override
    public String getTransactionType() {
        return "ATAT";
    }
}
