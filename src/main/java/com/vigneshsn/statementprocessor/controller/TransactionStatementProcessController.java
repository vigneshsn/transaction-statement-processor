package com.vigneshsn.statementprocessor.controller;

import com.vigneshsn.statementprocessor.models.Transaction;
import com.vigneshsn.statementprocessor.models.TransactionStatementResult;
import com.vigneshsn.statementprocessor.service.GenericTransactionStatementProcessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.vigneshsn.statementprocessor.util.TransactionValidationHelper.getDuplicateTransactions;
import static com.vigneshsn.statementprocessor.util.TransactionValidationHelper.getIncorrectTransactions;

@RestController
public class TransactionStatementProcessController {

    private GenericTransactionStatementProcessor transactionStatementProcessor;

    public TransactionStatementProcessController(GenericTransactionStatementProcessor transactionStatementProcessor) {
        this.transactionStatementProcessor = transactionStatementProcessor;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/process-transaction")
    public TransactionStatementResult processTransactionFile(MultipartFile file) {
        List<Transaction> transactionList = this.transactionStatementProcessor.process(file);
        return new TransactionStatementResult(getDuplicateTransactions(transactionList),
                getIncorrectTransactions(transactionList));
    }
}
