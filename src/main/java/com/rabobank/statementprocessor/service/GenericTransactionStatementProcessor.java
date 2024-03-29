package com.rabobank.statementprocessor.service;

import com.rabobank.statementprocessor.api.TransactionStatementProcessor;
import com.rabobank.statementprocessor.exceptions.DocumentTypeNotSupportedException;
import com.rabobank.statementprocessor.models.Transaction;
import com.rabobank.statementprocessor.util.DocumentHelper;
import com.rabobank.statementprocessor.util.DocumentType;
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
                .collect(Collectors.toMap(TransactionStatementProcessor::getType, processor -> processor));
    }

    private Optional<TransactionStatementProcessor> getTransactionStatementProcessorByFileType(final String requestFileType) {
        DocumentType documentType = DocumentType.lookUpByString(requestFileType.toUpperCase());

        if (documentType == DocumentType.UNKNOWN) {
            throw new DocumentTypeNotSupportedException("The requested file type not supported", requestFileType);
        }

        return Optional.ofNullable(transactionStatementProcessorMap.get(documentType));
    }

    public List<Transaction> process(final MultipartFile file) {
        return DocumentHelper.getFileExtensionFromFileName(file.getOriginalFilename())
                .map(fileType -> getTransactionStatementProcessorByFileType(fileType))
                .orElseThrow(() ->
                        new DocumentTypeNotSupportedException("The requested file type not supported", ""))
                .map(fileProcessor -> fileProcessor.process(file))
                .orElseThrow(() ->
                        new RuntimeException("Document processor not available for requested file getType"));

    }
}
