package com.lipari.bank.exception;

import java.math.BigDecimal;

/**
 * Lanciata quando si tenta un'operazione di prelievo o bonifico
 * con fondi insufficienti.
 */
public class InsufficientFundsException extends BankException {

    private final String iban;
    private final BigDecimal available;
    private final BigDecimal requested;

    public InsufficientFundsException(String iban, BigDecimal available, BigDecimal requested) {
        super(String.format(
                "Fondi insufficienti sul conto %s: disponibile=%.2f€, richiesto=%.2f€",
                iban, available, requested));
        this.iban = iban;
        this.available = available;
        this.requested = requested;
    }

    public String getIban()           { return iban; }
    public BigDecimal getAvailable()  { return available; }
    public BigDecimal getRequested()  { return requested; }
}
