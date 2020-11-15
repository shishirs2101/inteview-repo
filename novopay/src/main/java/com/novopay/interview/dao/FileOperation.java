package com.novopay.interview.dao;

import com.novopay.interview.exception.AlreadyExistException;
import com.novopay.interview.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public interface FileOperation {

    public void addCustomer(Customer customer) throws IOException, AlreadyExistException;
    public Map<Long, Customer> getCustomerRecords();
    public void writeLimitExhausted(String message);
    public  void removeCustomerDetailsFile();

}
