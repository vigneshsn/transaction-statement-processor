package com.vigneshsn.statementprocessor.util;

import java.util.Arrays;

public enum DocumentType {
    CSV("CSV"),XML("XML"), UNKNOWN("UNKNOWN");

    private String type;
    DocumentType(String type) {
        this.type = type;
    }

    public static DocumentType lookUpByString(String type) {
        return Arrays.stream(DocumentType.values())
                .filter(documentType -> type.equals(documentType.type))
                .findFirst()
                .orElse(DocumentType.UNKNOWN);
    }
}
