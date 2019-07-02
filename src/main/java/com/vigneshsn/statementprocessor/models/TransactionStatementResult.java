package com.vigneshsn.statementprocessor.models;

import java.io.Serializable;
import java.util.List;

public class TransactionStatementResult implements Serializable {

    public List<Transaction> getDuplicates() {
        return duplicates;
    }

    public List<Transaction> getIncorrectBalance() {
        return incorrectBalance;
    }

    private List<Transaction> duplicates;
    private List<Transaction> incorrectBalance;

    public TransactionStatementResult(List<Transaction> duplicates, List<Transaction> incorrectBalance) {
        this.duplicates = duplicates;
        this.incorrectBalance = incorrectBalance;
    }
}
