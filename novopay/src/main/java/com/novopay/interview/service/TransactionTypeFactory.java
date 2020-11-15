package com.novopay.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionTypeFactory {


    private  static final Map<String,Transactions>  transactionTypes = new HashMap<>();

    @Autowired
    public TransactionTypeFactory(List<Transactions> transactions) {
        transactions.forEach(transactions1 -> {
            transactionTypes.put(transactions1.getTransactionType(),transactions1);
        });
    }

    public static Transactions getTransactionTypes(String type)throws IllegalArgumentException{
        Transactions transactions = transactionTypes.get(type);
        if(transactions == null) throw new IllegalArgumentException("Unknown transaction type: " + type);
        return transactions;
    }
}
