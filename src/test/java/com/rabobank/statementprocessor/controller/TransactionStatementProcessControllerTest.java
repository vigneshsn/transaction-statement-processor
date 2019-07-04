package com.rabobank.statementprocessor.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.statementprocessor.TestHelper;
import com.rabobank.statementprocessor.exceptions.ApplicationExceptionHandler;
import com.rabobank.statementprocessor.exceptions.DocumentTypeNotSupportedException;
import com.rabobank.statementprocessor.exceptions.XMLParseException;
import com.rabobank.statementprocessor.models.TransactionStatementResult;
import com.rabobank.statementprocessor.service.GenericTransactionStatementProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TransactionStatementProcessControllerTest {

    private MockMvc mockMvc;
    private TransactionStatementProcessController transactionStatementProcessController;

    private static ResultMatcher ok = MockMvcResultMatchers.status().isOk();
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static String uploadUri = "/process-transaction";

    @Mock
    private GenericTransactionStatementProcessor genericTransactionStatementProcessor;

    @Before
    public void setUp() {
        transactionStatementProcessController = new TransactionStatementProcessController(genericTransactionStatementProcessor);
        mockMvc = MockMvcBuilders
                .standaloneSetup(transactionStatementProcessController)
                .setControllerAdvice(new ApplicationExceptionHandler())
                .build();
    }


    @Test
    public void should_process_transaction_file_and_return_duplicate_and_incorrect_transactions() throws Exception {

        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv", "".getBytes());

        Mockito.when(genericTransactionStatementProcessor.process(mockMultipartFile)).thenReturn(TestHelper.prepareTransactionList());

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.multipart(uploadUri)
                .file(mockMultipartFile))
                .andExpect(ok)
                .andReturn();

        TransactionStatementResult transactionStatementResult =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TransactionStatementResult.class);

        assertTrue("should have duplicate transactions",
                transactionStatementResult.getDuplicates().size() > 0);
        assertTrue("should have incorrect transactions",
                transactionStatementResult.getIncorrectBalance().size() > 0);

        assertEquals("171149", transactionStatementResult.getDuplicates().get(0).getId());
        assertEquals("158536", transactionStatementResult.getIncorrectBalance().get(0).getId());
    }


    @Test
    public void upload_unsupported_type_should_return_400_bad_request() throws Exception {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.unsupported", "unsupported", "".getBytes());
        Mockito.when(genericTransactionStatementProcessor.process(mockMultipartFile))
                .thenThrow(new DocumentTypeNotSupportedException("The document is not supported", "unsupported"));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart(uploadUri)
                .file(mockMultipartFile))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void upload_to_invalid_path_should_return_404() throws Exception {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.unsupported", "unsupported", "".getBytes());
        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/invalidpath")
                .file(mockMultipartFile))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void uploading_xml_file_with_invalid_schema_should_return_400_bad_request() throws Exception {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.xml", "xml", "<invalid></invalid>".getBytes());
        Mockito.when(genericTransactionStatementProcessor.process(mockMultipartFile))
                .thenThrow(new XMLParseException("error parsing xml file",
                        new RuntimeException("schema not valid"), "test.xml"));
        this.mockMvc.perform(MockMvcRequestBuilders.multipart(uploadUri)
                .file(mockMultipartFile))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void uploading_csv_file_with_invalid_schema_should_return_400_bad_request() throws Exception {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv", "<invalid></invalid>".getBytes());
        Mockito.when(genericTransactionStatementProcessor.process(mockMultipartFile))
                .thenThrow(new XMLParseException("error parsing csv file",
                        new RuntimeException("schema not valid"), "test.csv"));
        this.mockMvc.perform(MockMvcRequestBuilders.multipart(uploadUri)
                .file(mockMultipartFile))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void unknown_exception_should_return_internal_server_error() throws Exception {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.xml", "xml", "<invalid></invalid>".getBytes());

        Mockito.when(genericTransactionStatementProcessor.process(mockMultipartFile))
                .thenThrow(new RuntimeException("some exception is server"));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart(uploadUri)
                .file(mockMultipartFile))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
