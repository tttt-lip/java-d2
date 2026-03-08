package com.lipari.bank.repository;

import com.lipari.bank.model.Account;
import com.lipari.bank.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Archivio in-memory dei conti bancari, indicizzati per IBAN.
 * Restituisce i riferimenti diretti agli oggetti — nessuna copia difensiva.
 */
public class AccountRepository {

    private final Map<String, Account> storage = new HashMap<>();

    public void save(Account account) {
        storage.put(account.getIban(), account);
    }

    /**
     * Restituisce il riferimento diretto all'oggetto Account nel repository.
     * Le operazioni (deposit/withdraw) effettuate sull'oggetto restituito
     * modificano il conto effettivo.
     */
    public Optional<Account> findByIban(String iban) {
        return Optional.ofNullable(storage.get(iban));
    }

    public List<Account> findAll() {
        return new ArrayList<>(storage.values());
    }

    public List<Account> findByOwner(Customer owner) {
        return storage.values().stream()
                .filter(a -> a.getOwner().equals(owner))
                .collect(Collectors.toList());
    }

    public void delete(String iban) {
        storage.remove(iban);
    }

    public int count() {
        return storage.size();
    }
}
