package com.rabobank.statementprocessor.XMLProcessor;


import com.rabobank.statementprocessor.TestHelper;
import com.rabobank.statementprocessor.exceptions.XMLParseException;
import com.rabobank.statementprocessor.models.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class XMLStatementProcessorTest {

    private XMLStatementProcessor xmlStatementProcessor;

    @Before
    public void setUp(){
        xmlStatementProcessor = new XMLStatementProcessor();
    }

    @Test
    public void parsing_valid_xml_file_returns_transaction() {
        byte[] fileContent = TestHelper.readFileASBytes("records.xml");
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv", fileContent);
        List<Transaction> transactions = xmlStatementProcessor.process(mockMultipartFile);
        assertTrue("should contain transactions" , transactions.size() > 0);
    }

    @Test(expected = XMLParseException.class)
    public void parsing_invalid_xml_file_throws_exception() {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv",
                        "<invalid></invalid>".getBytes());
        xmlStatementProcessor.process(mockMultipartFile);
    }
}
