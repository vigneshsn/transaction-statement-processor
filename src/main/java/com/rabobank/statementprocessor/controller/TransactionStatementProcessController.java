package com.rabobank.statementprocessor.controller;

import com.rabobank.statementprocessor.models.Transaction;
import com.rabobank.statementprocessor.models.TransactionStatementResult;
import com.rabobank.statementprocessor.service.GenericTransactionStatementProcessor;
import com.rabobank.statementprocessor.util.TransactionValidationHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class TransactionStatementProcessController {

    private GenericTransactionStatementProcessor transactionStatementProcessor;

    public TransactionStatementProcessController(GenericTransactionStatementProcessor transactionStatementProcessor) {
        this.transactionStatementProcessor = transactionStatementProcessor;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/process-transaction", produces = "application/json")
    public TransactionStatementResult processTransactionFile(MultipartFile file) {
        List<Transaction> transactionList = this.transactionStatementProcessor.process(file);
        return new TransactionStatementResult(TransactionValidationHelper.getDuplicateTransactions(transactionList),
                TransactionValidationHelper.getIncorrectTransactions(transactionList));
    }
}
