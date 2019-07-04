package com.vigneshsn.statementprocessor.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vigneshsn.statementprocessor.TestUtils;
import com.vigneshsn.statementprocessor.models.TransactionStatementResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionStatementProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ResultMatcher ok = MockMvcResultMatchers.status().isOk();
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static String uploadUri = "/process-transaction";

    @Test
    public void should_process_csv_file_and_return_duplicate_and_incorrect_transactions() throws Exception {

        byte[] fileContent = TestUtils.readFileASBytes("records.csv");
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.csv", "csv", fileContent);

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

        assertEquals("112806", transactionStatementResult.getDuplicates().get(0).getId());
        assertEquals("123111", transactionStatementResult.getIncorrectBalance().get(0).getId());
    }

    @Test
    public void should_process_xml_file_and_return_duplicate_and_incorrect_transactions() throws Exception {

        byte[] fileContent = TestUtils.readFileASBytes("records.xml");
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.xml", "xml", fileContent);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.multipart(uploadUri)
                .file(mockMultipartFile))
                .andExpect(ok)
                .andReturn();

        TransactionStatementResult transactionStatementResult =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TransactionStatementResult.class);

        assertTrue("should have incorrect transactions",
                transactionStatementResult.getIncorrectBalance().size() > 0);
        assertEquals("171149", transactionStatementResult.getIncorrectBalance().get(0).getId());
    }

    @Test
    public void upload_unsupported_type_should_return_400_bad_request() throws Exception {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.unsupported", "unsupported", "".getBytes());
        this.mockMvc.perform(MockMvcRequestBuilders.multipart(uploadUri)
                .file(mockMultipartFile))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void upload_to_invalid_path_should_return_404() throws Exception {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "test.unsupported", "unsupported", "".getBytes());
        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/invalidpath")
                .file(mockMultipartFile))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
