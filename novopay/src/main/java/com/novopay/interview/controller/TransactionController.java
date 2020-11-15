package com.novopay.interview.controller;

import com.novopay.interview.dao.FileOperation;
import com.novopay.interview.exception.AlreadyExistException;
import com.novopay.interview.exception.LimitExhaustedException;
import com.novopay.interview.model.Customer;
import com.novopay.interview.model.TransactionDetails;
import com.novopay.interview.service.TransactionTypeFactory;
import com.novopay.interview.service.Transactions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Queue;

@Controller
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private FileOperation fileOperation;


    @PostMapping("/addCustomer")
    public ResponseEntity<String> addCustomer(@RequestBody(required = true) Customer customer) {
        HttpStatus respSttus;
        String message;
        logger.info("Adding the new customer record {}", customer);
        try {
            fileOperation.addCustomer(customer);
            respSttus = HttpStatus.OK;
            message = customer.toString() + " addeded successfully";
        } catch (IOException e) {
            respSttus = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Failed to added the customer";
            logger.error("Failed to added the customer {}, Error {}", customer, e);
        }catch (AlreadyExistException ae){
            respSttus = HttpStatus.BAD_REQUEST;
            message = "Account no already exist";
            logger.error("Failed to added the customer {}, Error {}", customer, ae);
        }

        return ResponseEntity.status(respSttus).body(message);
    }

    @PostMapping("/acct-txn")
    public ResponseEntity<String> acctTxn(@RequestBody TransactionDetails transactionDetails) {
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "transferred amount " + transactionDetails.getAmount() + " from account " + transactionDetails.getFromAcct()
                + " to " + transactionDetails.getToAcct();

        Transactions acctToAcctTxn = TransactionTypeFactory.getTransactionTypes("ATAT");
        try {

            acctToAcctTxn.initiateTransaction(transactionDetails);

        } catch (IOException e) {
            logger.error("Failed to process the transaction {}", transactionDetails);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Transaction failed";
        } catch (LimitExhaustedException limt) {
            httpStatus = HttpStatus.PAYMENT_REQUIRED;
            message = "Amount limit has been exceeded";
        } catch (IllegalArgumentException ill) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = "Account no does not exist";
        }
        return ResponseEntity.status(httpStatus).body(message);
    }

    @GetMapping("/save-rat")
    public ResponseEntity<String> saveRandomTxnForEachAcct() {
        Transactions randomTxn = TransactionTypeFactory.getTransactionTypes("RAT");
        Queue<TransactionDetails> transactionDetails = randomTxn.saveTransaction();
        return ResponseEntity.ok().body(transactionDetails.toString());
    }

    @GetMapping("/process-rat")
    public ResponseEntity<String> processRandomTxnForEachAcct() {
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "transaction completed";
        Transactions randomTxn = TransactionTypeFactory.getTransactionTypes("RAT");
        try {
            randomTxn.initiateTransaction(null);
        } catch (IOException e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            message = e.getMessage();
            logger.error("Failed to process the RAT {} ", e.getMessage());
        }
        return ResponseEntity.status(httpStatus).body(message);
    }
}
