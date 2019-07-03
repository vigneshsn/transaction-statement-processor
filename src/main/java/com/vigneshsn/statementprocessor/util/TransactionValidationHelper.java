package com.vigneshsn.statementprocessor.util;

import com.vigneshsn.statementprocessor.models.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionValidationHelper {

    public static List<Transaction> getDuplicateTransactions(final List<Transaction> transactionList) {
        return transactionList.stream()
                .filter(transaction -> Collections.frequency(transactionList, transaction) > 1)
                .collect(Collectors.toList());
    }

    public static List<Transaction> getIncorrectTransactions(final List<Transaction> transactionList) {
        return transactionList.stream()
                .filter(TransactionValidationHelper::isIncorrectBalance)
                .collect(Collectors.toList());
    }

    private static boolean isIncorrectBalance(final Transaction transaction) {
        return !transaction.getStartBalance().add(transaction.getMutation())
                .equals(transaction.getEndBalance());
    }

}
