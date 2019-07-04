package com.rabobank.statementprocessor.service;

import com.rabobank.statementprocessor.CSVProcessor.CSVStatementProcessor;
import com.rabobank.statementprocessor.TestHelper;
import com.rabobank.statementprocessor.XMLProcessor.XMLStatementProcessor;
import com.rabobank.statementprocessor.api.TransactionStatementProcessor;
import com.rabobank.statementprocessor.exceptions.DocumentTypeNotSupportedException;
import com.rabobank.statementprocessor.exceptions.XMLParseException;
import com.rabobank.statementprocessor.models.Transaction;
import com.rabobank.statementprocessor.util.DocumentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GenericTransactionStatementProcessorTest {

    private GenericTransactionStatementProcessor genericTransactionStatementProcessor;

    @Mock
    private XMLStatementProcessor xmlStatementProcessor;

    @Mock
    private CSVStatementProcessor csvStatementProcessor;

    @Before
    public void setUp() {
        List<TransactionStatementProcessor> transactionStatementProcessorList =
                Arrays.asList(xmlStatementProcessor, csvStatementProcessor);
        when(csvStatementProcessor.getType()).thenReturn(DocumentType.CSV);
        when(xmlStatementProcessor.getType()).thenReturn(DocumentType.XML);
        genericTransactionStatementProcessor = new GenericTransactionStatementProcessor(transactionStatementProcessorList);
    }

    @Test
    public void csv_file_with_valid_data_should_pass() {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv", "".getBytes());
        Mockito.when(csvStatementProcessor.process(mockMultipartFile)).thenReturn(TestHelper.prepareTransactionList());
        List<Transaction> result = genericTransactionStatementProcessor.process(mockMultipartFile);
        assertTrue("list should contain value", result.size() > 0);
        assertEquals("171149", result.get(0).getId());
        Mockito.verify(csvStatementProcessor, times(1)).process(mockMultipartFile);
    }

    @Test
    public void xml_file_with_valid_data_should_pass() {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.xml", "xml", "".getBytes());
        Mockito.when(xmlStatementProcessor.process(mockMultipartFile)).thenReturn(TestHelper.prepareTransactionList());
        List<Transaction> result = genericTransactionStatementProcessor.process(mockMultipartFile);
        assertTrue("list should contain value", result.size() > 0);
        assertEquals("171149", result.get(0).getId());
        Mockito.verify(xmlStatementProcessor, times(1)).process(mockMultipartFile);
    }

    @Test(expected = XMLParseException.class)
    public void xml_file_with_invalid_data_should_throw_exception() {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.xml", "xml", "<invalid></invalid>".getBytes());

        Mockito.when(xmlStatementProcessor.process(mockMultipartFile))
                .thenThrow(new XMLParseException("unable to parse", new RuntimeException("schema invalid"), "test.xml"));
        genericTransactionStatementProcessor.process(mockMultipartFile);
    }

    @Test
    public void csv_file_with_invalid_data_should_return_empty_list() {
        //open csv handles failure gracefully
        //exceptions are not thrown in case of invalid data
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv", "**INVALID**".getBytes());

        Mockito.when(csvStatementProcessor.process(mockMultipartFile))
                .thenReturn(Collections.emptyList());
        List<Transaction> transactions = genericTransactionStatementProcessor.process(mockMultipartFile);

        assertTrue("should return empty list", transactions.size() == 0);
    }

    @Test(expected = DocumentTypeNotSupportedException.class)
    public void unsupported_file_type_should_fail() {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.jpg", "jpg", "".getBytes());
        genericTransactionStatementProcessor.process(mockMultipartFile);
    }

    @Test(expected = DocumentTypeNotSupportedException.class)
    public void file_type_without_proper_extension_should_fail() {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test", "unknown", "".getBytes());
        genericTransactionStatementProcessor.process(mockMultipartFile);
    }

}
