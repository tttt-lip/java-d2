package com.lipari.bank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Conto di risparmio con tasso di interesse annuo.
 */
public final class SavingsAccount extends Account {

    /** Tasso annuo in percentuale (es: 3.0 → 3%). */
    private double interestRate;

    public SavingsAccount(String iban, BigDecimal initialBalance,
                          Customer owner, double interestRate) {
        super(iban, initialBalance, owner);
        if (interestRate < 0) {
            throw new IllegalArgumentException("Il tasso di interesse non può essere negativo");
        }
        this.interestRate = interestRate;
    }

    public double getInterestRate()           { return interestRate; }
    public void setInterestRate(double rate)  { this.interestRate = rate; }

    /** Accredita gli interessi annui sul saldo corrente. */
    public void applyInterest() {
        BigDecimal interest = getBalance()
                .multiply(BigDecimal.valueOf(interestRate / 100.0))
                .setScale(2, RoundingMode.HALF_UP);
        if (interest.compareTo(BigDecimal.ZERO) > 0) {
            deposit(interest);
        }
    }

    @Override
    public String toString() {
        return super.toString()
                + String.format(" | [RISPARMIO] tasso: %.2f%%", interestRate);
    }
}
