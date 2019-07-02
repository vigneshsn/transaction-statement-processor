package com.vigneshsn.statementprocessor.exceptions;

public class CSVParseException extends RuntimeException {

    public String getFileName() {
        return fileName;
    }

    private String fileName;

    public CSVParseException(String message, Throwable cause, String fileName) {
        super(message, cause);
        this.fileName = fileName;
    }
}
