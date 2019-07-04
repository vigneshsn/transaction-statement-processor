package com.rabobank.statementprocessor.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    @Test
    public void document_type_passing_supported_type_should_return_valid_type(){
        DocumentType type = DocumentType.lookUpByString("CSV");
        assertTrue(type == DocumentType.CSV);
    }

    @Test
    public void document_type_passing_unsupported_type_should_return_unknown(){
        DocumentType type = DocumentType.lookUpByString("somevalue");
        assertTrue(type == DocumentType.UNKNOWN);
    }
}
