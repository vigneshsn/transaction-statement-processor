package com.rabobank.statementprocessor.exceptions;

public class XMLParseException extends RuntimeException {
    private String fileName;

    public XMLParseException(String message, Throwable cause, String fileName) {
        super(message, cause);
        this.fileName = fileName;
    }
}
