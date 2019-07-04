package com.rabobank.statementprocessor.api;

import com.rabobank.statementprocessor.models.Transaction;
import com.rabobank.statementprocessor.util.DocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransactionStatementProcessor {
    List<Transaction> process(final MultipartFile file);
    DocumentType type();
}
