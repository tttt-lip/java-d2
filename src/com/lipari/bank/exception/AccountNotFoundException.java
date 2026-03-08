package com.lipari.bank.exception;

/**
 * Lanciata quando non esiste un conto con il dato IBAN nel repository.
 */
public class AccountNotFoundException extends BankException {

    public AccountNotFoundException(String iban) {
        super("Conto non trovato per IBAN: " + iban);
    }
}
