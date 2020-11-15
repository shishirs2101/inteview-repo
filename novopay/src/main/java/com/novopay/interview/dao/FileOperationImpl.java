package com.novopay.interview.dao;

import com.novopay.interview.exception.AlreadyExistException;
import com.novopay.interview.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileOperationImpl implements FileOperation {

    private final static Logger logger = LoggerFactory.getLogger(FileOperationImpl.class);
    private final static Map<Long, Customer> customerRecords = new ConcurrentHashMap<>();

    @Value("${customer.details}")
    private String customDetailFilePath;
    @Value("${customer.limit.exhausted}")
    private String limitExhausted;

    private String fileName = "customerDetail.txt";

    @Override
    public void addCustomer(Customer customer) throws IOException, AlreadyExistException {

        if (!CollectionUtils.isEmpty(customerRecords) && customerRecords.containsKey(customer.getAccountNumber())){
            logger.warn("Account No {} is already exist",customer.getAccountNumber());
            throw new AlreadyExistException("Account No"+customer.getAccountNumber()+ "is already exist");
        }

        File customeDetailsFile = new File(getAbsoluteFilePath());
        logger.info("Absolute file path {}", customeDetailsFile.getAbsoluteFile());

        try (RandomAccessFile stream = new RandomAccessFile(customeDetailsFile, "rw");
             FileChannel fileChannel = stream.getChannel();
             FileLock lock = fileChannel.tryLock()) {

            if (!customeDetailsFile.exists()) customeDetailsFile.createNewFile();

            long fileLength = customeDetailsFile.length();
            if (fileLength == 0)
                stream.writeChars(customer.print());
            else {
                stream.seek(fileLength);
                stream.writeChars("\n" + customer.print());
            }

            customerRecords.put(customer.getAccountNumber(), customer);
            logger.info("File {} has been created successfully, records:{}", customeDetailsFile.getName(), customerRecords.toString());
            lock.release();
        } catch (final OverlappingFileLockException e) {
            logger.error("Lock has already been occupied, error message {}", e.getMessage());
        }
    }

    @Override
    public Map<Long, Customer> getCustomerRecords() {
        return customerRecords;
    }

    @Override
    public void writeLimitExhausted(String message) {
        File customeDetailsFile = new File(limitExhausted + "limitExhaustedAccount.txt");
        logger.info("Absolute limit file path {}", customeDetailsFile.getAbsoluteFile());

        try (RandomAccessFile stream = new RandomAccessFile(customeDetailsFile, "rw")) {
            if (!customeDetailsFile.exists()) customeDetailsFile.createNewFile();

            long fileLength = customeDetailsFile.length();
            if (fileLength == 0)
                stream.writeChars(message);
            else {
                stream.seek(fileLength);
                stream.writeChars("\n" + message);
            }

        } catch (IOException e) {
            logger.error("Failed to create a error file error message {}", e.getMessage());
        }
    }

    @Override
    public void removeCustomerDetailsFile() {
        logger.warn("Removing the file as server has restarted");
        File file = new File(getAbsoluteFilePath());
        if (file.delete()){
            logger.warn("File {} has deleted successfully",getAbsoluteFilePath());
        }
    }

    private String getAbsoluteFilePath() {
        return customDetailFilePath + fileName;
    }
}
