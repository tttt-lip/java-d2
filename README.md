# 🔧 LipariBank Broken Project — Day 2

Progetto Java 21 didattico con **3 bug logici** nascosti.
Il codice **compila senza errori** ma si comporta in modo scorretto a runtime.
Il tuo obiettivo è trovarli tutti e tre.

---

## Struttura del progetto

```
liparibank-broken-day2/
├── src/com/lipari/bank/
│   ├── model/
│   │   ├── CustomerType.java
│   │   ├── TransactionType.java
│   │   ├── Customer.java            (equals/hashCode su fiscalCode)
│   │   ├── Transaction.java         (record)
│   │   ├── Account.java             (sealed class)
│   │   ├── CheckingAccount.java     (final)
│   │   └── SavingsAccount.java      (final)
│   ├── exception/
│   │   ├── BankException.java
│   │   ├── InsufficientFundsException.java
│   │   ├── AccountNotFoundException.java
│   │   └── InvalidTransactionException.java
│   ├── repository/
│   │   ├── CustomerRepository.java
│   │   └── AccountRepository.java
│   ├── service/
│   │   └── TransferService.java
│   └── cli/
│       └── BankConsoleDayTwo.java
├── README.md
└── compile-and-run.sh
```

---

## Compilazione e avvio

### Prerequisiti

- Java 17+ (la struttura usa sealed class, record, text block)

### 1. Crea la cartella di output

```bash
mkdir -p out
```

### 2. Compila

```bash
javac -d out \
  src/com/lipari/bank/exception/BankException.java \
  src/com/lipari/bank/exception/InsufficientFundsException.java \
  src/com/lipari/bank/exception/AccountNotFoundException.java \
  src/com/lipari/bank/exception/InvalidTransactionException.java \
  src/com/lipari/bank/model/CustomerType.java \
  src/com/lipari/bank/model/TransactionType.java \
  src/com/lipari/bank/model/Customer.java \
  src/com/lipari/bank/model/Transaction.java \
  src/com/lipari/bank/model/Account.java \
  src/com/lipari/bank/model/CheckingAccount.java \
  src/com/lipari/bank/model/SavingsAccount.java \
  src/com/lipari/bank/repository/CustomerRepository.java \
  src/com/lipari/bank/repository/AccountRepository.java \
  src/com/lipari/bank/service/TransferService.java \
  src/com/lipari/bank/cli/BankConsoleDayTwo.java
```

Oppure usa lo script:

```bash
chmod +x compile-and-run.sh
./compile-and-run.sh
```

### 3. Esegui

```bash
java -cp out com.lipari.bank.cli.BankConsoleDayTwo
```

---

## 🕵️ Le tue 3 missioni

---

### MISSIONE 1 — Il cliente non viene trovato nel repository

**Sintomo:** Il programma salva un cliente nel repository e poi tenta di
recuperarlo usando il codice fiscale scritto in minuscolo. Nonostante il
cliente esista, la ricerca restituisce **"NON TROVATO"**.

---

### MISSIONE 2 — Il bonifico non scala il saldo del conto sorgente

**Sintomo:** Dopo l'esecuzione di un bonifico di 300€, il saldo del conto
destinatario aumenta correttamente (+300€), ma il saldo del conto sorgente
rimane **invariato** — i soldi vengono "copiati", non trasferiti.

---

### MISSIONE 3 — Eccezione durante la pulizia della lista clienti

**Sintomo:** Il programma tenta di rimuovere dalla lista tutti i clienti
di tipo BUSINESS, ma lancia una **ConcurrentModificationException** prima
di completare l'operazione. Nessun cliente viene rimosso.

---

## ✅ Obiettivo finale

Quando hai trovato e corretto tutti e 3 i bug, hai completato la missione!

- La ricerca per CF deve funzionare indipendentemente dal case
- Il saldo source deve diminuire dopo il bonifico
- La lista deve essere filtrata senza eccezioni
