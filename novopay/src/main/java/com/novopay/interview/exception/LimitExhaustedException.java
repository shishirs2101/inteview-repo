package com.novopay.interview.exception;

public class LimitExhaustedException extends RuntimeException{
    public LimitExhaustedException(String message) {
        super(message);
    }
}
