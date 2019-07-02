package com.vigneshsn.statementprocessor.models;

import com.opencsv.bean.CsvBindByName;

import java.math.BigDecimal;

public class Transaction {

    @CsvBindByName(column = "Reference")
    private String id;
    @CsvBindByName(column = "Start Balance")
    private BigDecimal balance;

    public Transaction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
