package com.vigneshsn.statementprocessor.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatementResult implements Serializable {

    private List<Transaction> duplicates;
    private List<Transaction> incorrectBalance;
}
