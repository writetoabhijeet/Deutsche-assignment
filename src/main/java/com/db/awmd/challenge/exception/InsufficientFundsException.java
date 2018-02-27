package com.db.awmd.challenge.exception;

/**
 * Created by USER on 25-02-2018.
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}