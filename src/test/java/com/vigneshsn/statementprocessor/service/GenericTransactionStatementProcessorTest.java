package com.vigneshsn.statementprocessor.service;

import com.vigneshsn.statementprocessor.CSVProcessor.CSVStatementProcessor;
import com.vigneshsn.statementprocessor.TestUtils;
import com.vigneshsn.statementprocessor.XMLProcessor.XMLStatementProcessor;
import com.vigneshsn.statementprocessor.api.TransactionStatementProcessor;
import com.vigneshsn.statementprocessor.exceptions.DocumentTypeNotSupportedException;
import com.vigneshsn.statementprocessor.exceptions.XMLParseException;
import com.vigneshsn.statementprocessor.models.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GenericTransactionStatementProcessorTest {

    private GenericTransactionStatementProcessor genericTransactionStatementProcessor;

    @Before
    public void setUp(){
        List<TransactionStatementProcessor> transactionStatementProcessorList =
                Arrays.asList(new CSVStatementProcessor(), new XMLStatementProcessor());
        genericTransactionStatementProcessor = new GenericTransactionStatementProcessor(transactionStatementProcessorList);
    }

    @Test
    public void csv_file_with_valid_data_should_pass() {
        byte[] transactionDataBytes = TestUtils.readFileASBytes("records.csv");
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv", transactionDataBytes);
        List<Transaction> result  = genericTransactionStatementProcessor.process(mockMultipartFile);
        assertTrue("list should contain value", result.size() > 0);
        assertEquals("147534" , result.get(0).getId());
    }

    @Test
    public void xml_file_with_valid_data_should_pass() {
        byte[] transactionDataBytes = TestUtils.readFileASBytes("records.xml");
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.xml", "xml", transactionDataBytes);
        List<Transaction> result  = genericTransactionStatementProcessor.process(mockMultipartFile);
        assertTrue("list should contain value", result.size() > 0);
        assertEquals("171149" , result.get(0).getId());
    }

    @Test(expected = XMLParseException.class)
    public void xml_file_with_invalid_data_should_throw_exception() {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.xml", "xml", "<invalid></invalid>".getBytes());
        genericTransactionStatementProcessor.process(mockMultipartFile);
    }

    @Test
    public void csv_file_with_invalid_data_should_return_empty_list() {
        //open csv handles failure gracefully
        //exceptions are not thrown in case of invalid data
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv", "**INVALID**".getBytes());
        List<Transaction> result  = genericTransactionStatementProcessor.process(mockMultipartFile);
        assertTrue("Transaction list should be empty", result.size() == 0);
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
