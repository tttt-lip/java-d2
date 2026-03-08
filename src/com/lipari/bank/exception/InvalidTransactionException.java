package com.lipari.bank.exception;

/**
 * Lanciata quando i parametri di una transazione non sono validi
 * (es. importo negativo o nullo, conti identici per un bonifico).
 */
public class InvalidTransactionException extends BankException {

    public InvalidTransactionException(String message) {
        super(message);
    }
}
