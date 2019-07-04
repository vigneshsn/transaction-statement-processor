package com.rabobank.statementprocessor.CSVProcessor;

import com.rabobank.statementprocessor.TestHelper;
import com.rabobank.statementprocessor.models.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CSVStatementProcessorTest {

    private CSVStatementProcessor csvStatementProcessor;

    @Before
    public void setUp() {
        csvStatementProcessor = new CSVStatementProcessor();
    }

    @Test
    public void parsing_valid_csv_return_transactions() {
        byte[] fileContent = TestHelper.readFileASBytes("records.csv");
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv", fileContent);
        List<Transaction> transactions = csvStatementProcessor.process(mockMultipartFile);
        assertTrue("should contain value", transactions.size() > 0);
    }

    @Test
    public void parsing_invalid_csv_return_empty_list() {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv", "invalid".getBytes());
        List<Transaction> transactions = csvStatementProcessor.process(mockMultipartFile);
        assertTrue("should return empty list", transactions.size() == 0);
    }
}

