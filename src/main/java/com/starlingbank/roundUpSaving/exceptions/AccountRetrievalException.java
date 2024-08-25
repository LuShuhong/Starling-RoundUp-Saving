package com.starlingbank.roundUpSaving.exceptions;

public class AccountRetrievalException extends RuntimeException{
    public AccountRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
