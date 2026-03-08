package com.lipari.bank.exception;

/**
 * Eccezione base per tutte le eccezioni di dominio di LipariBank.
 */
public class BankException extends RuntimeException {

    public BankException(String message) {
        super(message);
    }

    public BankException(String message, Throwable cause) {
        super(message, cause);
    }
}
