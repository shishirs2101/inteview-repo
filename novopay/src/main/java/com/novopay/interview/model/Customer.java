package com.novopay.interview.model;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Customer {
    private int id;
    private String name;
    private long accountNumber;
    private double giftBalance = 50000.00;
    private double giftCardBalance = 1000.00;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        if (Pattern.matches("[5][0-9]{5}", String.valueOf(accountNumber)))
            this.accountNumber = accountNumber;
        else
            throw new IllegalArgumentException("Invalid account No, account no must start with 5 and it's must be 6");
    }


    public double getGiftBalance() {
        return giftBalance;
    }

    public void setGiftBalance(double giftBalance) {
        this.giftBalance = giftBalance;
    }

    public double getGiftCardBalance() {
        return giftCardBalance;
    }

    public void setGiftCardBalance(double giftCardBalance) {
        this.giftCardBalance = giftCardBalance;
    }

    public String print() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(id).append(",")
                .append(name).append(",")
                .append(accountNumber).append(",")
                .append(giftBalance).append(",")
                .append(giftCardBalance).append(")");
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountNumber=" + accountNumber +
                ", giftBalance=" + giftBalance +
                ", giftCardBalance=" + giftCardBalance +
                '}';
    }
}
