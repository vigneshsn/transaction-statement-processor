package com.rabobank.statementprocessor.exceptions;

public class DocumentTypeNotSupportedException extends RuntimeException {

    private String unSupportedType;

    public String getUnSupportedType() {
        return unSupportedType;
    }

    public DocumentTypeNotSupportedException(String message, String unSupportedType) {
        super(message);
        this.unSupportedType = unSupportedType;
    }
}
