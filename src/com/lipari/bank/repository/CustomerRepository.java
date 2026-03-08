package com.lipari.bank.repository;

import com.lipari.bank.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Archivio in-memory dei clienti, indicizzati per codice fiscale.
 */
public class CustomerRepository {

    private final Map<String, Customer> storage = new HashMap<>();

    /**
     * Salva un cliente normalizzando il codice fiscale a uppercase+trim
     * prima di usarlo come chiave.
     */
    public void save(Customer customer) {
        String key = customer.getFiscalCode().toUpperCase().trim();
        storage.put(key, customer);
    }

    /**
     * Cerca un cliente per codice fiscale.
     */
    public Optional<Customer> findByFiscalCode(String fiscalCode) {
        return Optional.ofNullable(storage.get(fiscalCode));
    }

    public List<Customer> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean existsByFiscalCode(String fiscalCode) {
        return storage.containsKey(fiscalCode.toUpperCase().trim());
    }

    public void delete(String fiscalCode) {
        storage.remove(fiscalCode.toUpperCase().trim());
    }

    public int count() {
        return storage.size();
    }
}
