#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
#  LipariBank Broken Project — Day 2
#  Compilazione e avvio
# ─────────────────────────────────────────────────────────────────────────────

set -e

SOURCES=(
  src/com/lipari/bank/exception/BankException.java
  src/com/lipari/bank/exception/InsufficientFundsException.java
  src/com/lipari/bank/exception/AccountNotFoundException.java
  src/com/lipari/bank/exception/InvalidTransactionException.java
  src/com/lipari/bank/model/CustomerType.java
  src/com/lipari/bank/model/TransactionType.java
  src/com/lipari/bank/model/Customer.java
  src/com/lipari/bank/model/Transaction.java
  src/com/lipari/bank/model/Account.java
  src/com/lipari/bank/model/CheckingAccount.java
  src/com/lipari/bank/model/SavingsAccount.java
  src/com/lipari/bank/repository/CustomerRepository.java
  src/com/lipari/bank/repository/AccountRepository.java
  src/com/lipari/bank/service/TransferService.java
  src/com/lipari/bank/cli/BankConsoleDayTwo.java
)

echo "==> Creazione cartella output..."
mkdir -p out

echo "==> Compilazione con Java $(java -version 2>&1 | head -1)..."
javac -d out "${SOURCES[@]}"

echo "==> Compilazione completata. Avvio applicazione..."
echo "─────────────────────────────────────────────────────────────────────────────"
java -cp out com.lipari.bank.cli.BankConsoleDayTwo
