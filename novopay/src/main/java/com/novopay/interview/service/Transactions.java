package com.novopay.interview.service;

import com.novopay.interview.exception.LimitExhaustedException;
import com.novopay.interview.model.Customer;
import com.novopay.interview.model.TransactionDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Service
public interface Transactions {
    public void initiateTransaction(TransactionDetails transactionDetails) throws IOException;

    public void amtTransaction(long fromAcct, long toAcct, double amount) throws LimitExhaustedException, IllegalArgumentException, IOException;


    public Queue<TransactionDetails> saveTransaction();

    public String getTransactionType();
}
