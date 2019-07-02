package com.vigneshsn.statementprocessor.controller;

import com.vigneshsn.statementprocessor.models.Transaction;
import com.vigneshsn.statementprocessor.service.GenericTransactionStatementProcessor;
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

    @RequestMapping(method = RequestMethod.POST, path = "/process-file")
    public List<Transaction> processStatementForFileUpload(MultipartFile file) {
        return this.transactionStatementProcessor.process(file);
    }
}
