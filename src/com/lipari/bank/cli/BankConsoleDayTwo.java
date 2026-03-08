package com.lipari.bank.cli;

import com.lipari.bank.exception.BankException;
import com.lipari.bank.model.*;
import com.lipari.bank.repository.AccountRepository;
import com.lipari.bank.repository.CustomerRepository;
import com.lipari.bank.service.TransferService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;

/**
 * Entry point del progetto LipariBank — Day 2.
 *
 * Esegue tre scenari di test sequenziali, ognuno dei quali
 * dimostra uno dei tre bug logici presenti nel progetto.
 */
public class BankConsoleDayTwo {

    // ─── Repository e servizi ────────────────────────────────────────────────

    private final CustomerRepository customerRepo = new CustomerRepository();
    private final AccountRepository   accountRepo  = new AccountRepository();
    private final TransferService     transferSvc  = new TransferService(accountRepo);

    // ─── Entry point ─────────────────────────────────────────────────────────

    public static void main(String[] args) {
        new BankConsoleDayTwo().runAllScenarios();
    }

    private void runAllScenarios() {
        printBanner();
        scenario1_customerLookup();
        scenario2_transfer();
        scenario3_concurrentModification();
        printFooter();
    }

    private void scenario1_customerLookup() {
        sep("SCENARIO 1 — Ricerca cliente per codice fiscale");

        Customer mario = new Customer(
                "RSSMRA85M01H501Z", "Mario", "Rossi", CustomerType.PRIVATE);
        customerRepo.save(mario);

        System.out.println("  Cliente salvato    : " + mario.getFiscalCode());
        System.out.println("  Chiave usata intern: "
                + mario.getFiscalCode().toUpperCase().trim() + "  (normalizzata da save())");

        String searchKey = "rssmra85m01h501z";   // stesso CF, ma minuscolo
        System.out.println("  Chiave di ricerca  : " + searchKey + "  (NON normalizzata)");

        Optional<Customer> found = customerRepo.findByFiscalCode(searchKey);

        if (found.isPresent()) {
            System.out.println("  Risultato          : TROVATO → " + found.get());
        } else {
            System.out.println("  Risultato          : *** NON TROVATO ***");
        }
    }

    // ─── SCENARIO 2 ──────────────────────────────────────────────────────────

    private void scenario2_transfer() {
        sep("SCENARIO 2 — Bonifico bancario");

        Customer mario = new Customer(
                "RSSMRA85M01H501Z", "Mario", "Rossi", CustomerType.PRIVATE);
        Customer anna  = new Customer(
                "BNCNNA90L50C351X", "Anna",  "Bianchi", CustomerType.BUSINESS);

        CheckingAccount sourceAcc = new CheckingAccount(
                "IT10A0542811101000000001001",
                new BigDecimal("1000.00"),
                mario,
                new BigDecimal("500.00"));

        CheckingAccount targetAcc = new CheckingAccount(
                "IT10A0542811101000000002002",
                new BigDecimal("500.00"),
                anna,
                BigDecimal.ZERO);

        accountRepo.save(sourceAcc);
        accountRepo.save(targetAcc);

        BigDecimal transferAmount = new BigDecimal("300.00");

        System.out.printf("  Source [%s] saldo PRIMA : %8.2f€%n",
                sourceAcc.getIban(), accountRepo.findByIban(sourceAcc.getIban()).get().getBalance());
        System.out.printf("  Target [%s] saldo PRIMA : %8.2f€%n",
                targetAcc.getIban(), accountRepo.findByIban(targetAcc.getIban()).get().getBalance());

        System.out.println("  Esecuzione bonifico: " + transferAmount + "€ ...");

        try {
            transferSvc.executeTransfer(
                    sourceAcc.getIban(), targetAcc.getIban(), transferAmount);
        } catch (BankException e) {
            System.out.println("  ERRORE INATTESO: " + e.getMessage());
            return;
        }

        BigDecimal sourceAfter = accountRepo.findByIban(sourceAcc.getIban()).get().getBalance();
        BigDecimal targetAfter = accountRepo.findByIban(targetAcc.getIban()).get().getBalance();
        BigDecimal expectedSource = new BigDecimal("700.00");
        BigDecimal expectedTarget = new BigDecimal("800.00");

        System.out.printf("  Source [%s] saldo DOPO  : %8.2f€  %s%n",
                sourceAcc.getIban(), sourceAfter,
                sourceAfter.compareTo(expectedSource) == 0 ? "✓" : "← saldo invariato!");
        System.out.printf("  Target [%s] saldo DOPO  : %8.2f€  %s%n",
                targetAcc.getIban(), targetAfter,
                targetAfter.compareTo(expectedTarget) == 0 ? "✓" : "← valore inatteso!");
    }

    // ─── SCENARIO 3 ──────────────────────────────────────────────────────────

    private void scenario3_concurrentModification() {
        sep("SCENARIO 3 — Rimozione clienti business dalla lista");

        List<Customer> customers = new ArrayList<>(List.of(
                new Customer("RSSMRA85M01H501Z", "Mario",   "Rossi",    CustomerType.PRIVATE),
                new Customer("BNCNNA90L50C351X", "Anna",    "Bianchi",  CustomerType.BUSINESS),
                new Customer("VRDBRT75L10C351Y", "Roberto", "Verdi",    CustomerType.BUSINESS),
                new Customer("NRIGNS80A01H501Q", "Ignazio", "Nardello", CustomerType.PRIVATE)
        ));

        System.out.println("  Lista prima della rimozione (" + customers.size() + " clienti):");
        customers.forEach(c -> System.out.println("    • " + c));

        System.out.println("  Avvio rimozione clienti BUSINESS con for-each ...");

        try {
            removeBusinessCustomers(customers);
            // Se arrivassimo qui la lista dovrebbe avere solo PRIVATE
            System.out.println("  Lista dopo la rimozione (" + customers.size() + " clienti):");
            customers.forEach(c -> System.out.println("    • " + c));
        } catch (ConcurrentModificationException e) {
            System.out.println("  ERRORE: ConcurrentModificationException!");
        }
    }

    private void removeBusinessCustomers(List<Customer> customers) {
        for (Customer c : customers) {                    // iteratore fail-fast
            if (c.getCustomerType() == CustomerType.BUSINESS) {
                customers.remove(c);
            }
        }
    }

    // ─── Utility di stampa ───────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println("""
                ╔══════════════════════════════════════════════════════════╗
                ║        LIPARIBANK — Broken Project Day 2                 ║
                ║        Scenario di test: trova i 3 bug!                  ║
                ╚══════════════════════════════════════════════════════════╝
                """);
    }

    private static void sep(String title) {
        System.out.println("\n══ " + title + " " + "═".repeat(Math.max(0, 55 - title.length())));
    }

    private static void printFooter() {
        System.out.println("""

                ══════════════════════════════════════════════════════════
                  Hai trovato tutti e 3 i bug? Fixali e riesegui!
                ══════════════════════════════════════════════════════════
                """);
    }
}
