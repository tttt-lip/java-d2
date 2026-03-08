package com.lipari.bank.model;

import java.util.Objects;

public class Customer {

    private final String fiscalCode;
    private String firstName;
    private String lastName;
    private CustomerType customerType;

    public Customer(String fiscalCode, String firstName, String lastName, CustomerType customerType) {
        if (fiscalCode == null || fiscalCode.isBlank()) {
            throw new IllegalArgumentException("Il codice fiscale è obbligatorio");
        }
        this.fiscalCode = fiscalCode.trim();
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerType = customerType;
    }

    public String getFiscalCode()          { return fiscalCode; }
    public String getFirstName()           { return firstName; }
    public String getLastName()            { return lastName; }
    public CustomerType getCustomerType()  { return customerType; }

    public void setFirstName(String firstName)             { this.firstName = firstName; }
    public void setLastName(String lastName)               { this.lastName = lastName; }
    public void setCustomerType(CustomerType customerType) { this.customerType = customerType; }

    /** Uguaglianza basata esclusivamente sul codice fiscale. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer c)) return false;
        return fiscalCode.equalsIgnoreCase(c.fiscalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiscalCode.toUpperCase());
    }

    @Override
    public String toString() {
        return String.format("%s %s [%s] — %s",
                firstName, lastName, fiscalCode, customerType.getLabel());
    }
}
