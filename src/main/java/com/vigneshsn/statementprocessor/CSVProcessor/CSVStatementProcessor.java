package com.vigneshsn.statementprocessor.CSVProcessor;

import com.vigneshsn.statementprocessor.api.TransactionStatementProcessor;
import com.vigneshsn.statementprocessor.models.Transaction;
import com.vigneshsn.statementprocessor.util.DocumentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CSVStatementProcessor implements TransactionStatementProcessor {

    @Override
    public List<Transaction> process(final MultipartFile file) {
        return CSVHelper.parse(file, Transaction.class);
    }

    @Override
    public DocumentType type() {
        return DocumentType.CSV;
    }

}
