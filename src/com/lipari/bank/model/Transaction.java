package com.lipari.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Rappresenta un singolo movimento contabile.
 * Immutabile per design (record Java 21).
 */
public record Transaction(
        TransactionType type,
        BigDecimal amount,
        String description,
        LocalDateTime timestamp) {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /** Compact constructor con validazione. */
    public Transaction {
        Objects.requireNonNull(type,        "Il tipo transazione è obbligatorio");
        Objects.requireNonNull(amount,      "L'importo è obbligatorio");
        Objects.requireNonNull(description, "La descrizione è obbligatoria");
        Objects.requireNonNull(timestamp,   "Il timestamp è obbligatorio");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("L'importo non può essere negativo");
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %-10s %10.2f€  %s",
                timestamp.format(FMT), type.getLabel(), amount, description);
    }
}
