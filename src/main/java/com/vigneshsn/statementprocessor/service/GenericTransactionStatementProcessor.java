package com.vigneshsn.statementprocessor.service;

import com.vigneshsn.statementprocessor.api.TransactionStatementProcessor;
import com.vigneshsn.statementprocessor.exceptions.DocumentTypeNotSupportedException;
import com.vigneshsn.statementprocessor.models.Transaction;
import com.vigneshsn.statementprocessor.util.DocumentHelper;
import com.vigneshsn.statementprocessor.util.DocumentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenericTransactionStatementProcessor {

    private final Map<DocumentType, TransactionStatementProcessor> transactionStatementProcessorMap;

    public GenericTransactionStatementProcessor(final List<TransactionStatementProcessor> statementProcessors) {
        transactionStatementProcessorMap = statementProcessors.stream()
                .collect(Collectors.toMap(TransactionStatementProcessor::type, processor -> processor));
    }

    private Optional<TransactionStatementProcessor> getTransactionStatementProcessorByFileType(final String requestFileType) {
        DocumentType documentType = DocumentType.lookUpByString(requestFileType.toUpperCase());

        if (documentType == DocumentType.UNKNOWN) {
            throw new DocumentTypeNotSupportedException("The requested file type not supported", requestFileType);
        }

        return Optional.ofNullable(transactionStatementProcessorMap.get(documentType));
    }

    public List<Transaction> process(final MultipartFile file) {
        Optional<TransactionStatementProcessor> transactionStatementProcessor =
                DocumentHelper.getFileExtensionFromFileName(file.getOriginalFilename())
                .map(fileType -> getTransactionStatementProcessorByFileType(fileType))
                .orElseThrow(() -> new DocumentTypeNotSupportedException("File name is not correct", ""));

        if(transactionStatementProcessor.isPresent()) {
            return transactionStatementProcessor.get().process(file);
        }

        throw new RuntimeException("Document processor not available for requested file type");
    }
}
