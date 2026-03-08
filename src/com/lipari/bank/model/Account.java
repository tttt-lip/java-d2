package com.lipari.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe base sealed per i conti bancari.
 * Permette solo CheckingAccount e SavingsAccount come sottoclassi dirette.
 */
public sealed class Account permits CheckingAccount, SavingsAccount {

    private final String iban;
    private BigDecimal balance;
    private final Customer owner;
    private final List<Transaction> transactions;

    public Account(String iban, BigDecimal initialBalance, Customer owner) {
        if (iban == null || iban.isBlank()) {
            throw new IllegalArgumentException("IBAN non valido");
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Il saldo iniziale non può essere negativo");
        }
        this.iban = iban;
        this.balance = initialBalance;
        this.owner = owner;
        this.transactions = new ArrayList<>();
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public String getIban()      { return iban; }
    public BigDecimal getBalance() { return balance; }
    public Customer getOwner()   { return owner; }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    // ─── Operazioni ─────────────────────────────────────────────────────────

    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'importo del deposito deve essere positivo");
        }
        this.balance = this.balance.add(amount);
        transactions.add(new Transaction(
                TransactionType.DEPOSIT, amount, "Deposito", LocalDateTime.now()));
    }

    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'importo del prelievo deve essere positivo");
        }
        BigDecimal newBalance = balance.subtract(amount);
        if (newBalance.compareTo(getMinBalance()) < 0) {
            throw new IllegalStateException(String.format(
                    "Fondi insufficienti sul conto %s: disponibile=%.2f€, richiesto=%.2f€",
                    iban, balance, amount));
        }
        this.balance = newBalance;
        transactions.add(new Transaction(
                TransactionType.WITHDRAWAL, amount, "Prelievo", LocalDateTime.now()));
    }

    /** Soglia minima di saldo. Override nelle sottoclassi. */
    protected BigDecimal getMinBalance() {
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return String.format("IBAN: %-28s | Saldo: %10.2f€ | Titolare: %s",
                iban, balance, owner);
    }
}
