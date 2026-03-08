package com.lipari.bank.service;

import com.lipari.bank.exception.AccountNotFoundException;
import com.lipari.bank.exception.InvalidTransactionException;
import com.lipari.bank.model.Account;
import com.lipari.bank.model.CheckingAccount;
import com.lipari.bank.model.Transaction;
import com.lipari.bank.model.TransactionType;
import com.lipari.bank.repository.AccountRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Servizio che gestisce i bonifici bancari tra conti.
 */
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Esegue un bonifico dal conto sorgente al conto destinatario.
     *
     * @param sourceIban IBAN del conto sorgente
     * @param targetIban IBAN del conto destinatario
     * @param amount     importo da trasferire (deve essere positivo)
     */
    public void executeTransfer(String sourceIban, String targetIban, BigDecimal amount) {

        if (sourceIban.equals(targetIban)) {
            throw new InvalidTransactionException(
                    "Il conto sorgente e il destinatario non possono coincidere");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException(
                    "L'importo del bonifico deve essere positivo");
        }

        Account source = accountRepository.findByIban(sourceIban)
                .orElseThrow(() -> new AccountNotFoundException(sourceIban));
        Account target = accountRepository.findByIban(targetIban)
                .orElseThrow(() -> new AccountNotFoundException(targetIban));

        source.withdraw(amount);
        target.deposit(amount);

        System.out.printf("  [TransferService] Bonifico di %.2f€ da %s a %s eseguito.%n",
                amount, sourceIban, targetIban);
    }

    /**
     * Restituisce il saldo corrente di un conto.
     */
    public BigDecimal getBalance(String iban) {
        return accountRepository.findByIban(iban)
                .map(Account::getBalance)
                .orElseThrow(() -> new AccountNotFoundException(iban));
    }
}
