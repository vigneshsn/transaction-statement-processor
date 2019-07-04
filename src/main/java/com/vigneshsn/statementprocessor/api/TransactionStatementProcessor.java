package com.vigneshsn.statementprocessor.api;

import com.vigneshsn.statementprocessor.models.Transaction;
import com.vigneshsn.statementprocessor.util.DocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransactionStatementProcessor {
    List<Transaction> process(final MultipartFile file);
    DocumentType type();
}
