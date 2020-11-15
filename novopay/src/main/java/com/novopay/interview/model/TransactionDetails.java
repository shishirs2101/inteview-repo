package com.novopay.interview.model;

import org.springframework.stereotype.Service;


@Service
public  class TransactionDetails  {
    private long fromAcct;
    private long toAcct;
    private double amount;

    public TransactionDetails(long fromAcct, long toAcct, double amount) {
        this.fromAcct = fromAcct;
        this.toAcct = toAcct;
        this.amount = amount;
    }

    public TransactionDetails() {
    }

    public long getFromAcct() {
        return fromAcct;
    }


    public long getToAcct() {
        return toAcct;
    }

    public double getAmount() {
        return amount;
    }

    public void setFromAcct(long fromAcct) {
        this.fromAcct = fromAcct;
    }

    public void setToAcct(long toAcct) {
        this.toAcct = toAcct;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionDetails{" +
                "fromAcct=" + fromAcct +
                ", toAcct=" + toAcct +
                ", amount=" + amount +
                '}';
    }
}
