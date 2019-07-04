package com.rabobank.statementprocessor.XMLProcessor;

import com.rabobank.statementprocessor.api.TransactionStatementProcessor;
import com.rabobank.statementprocessor.exceptions.XMLParseException;
import com.rabobank.statementprocessor.models.Transaction;
import com.rabobank.statementprocessor.models.Transactions;
import com.rabobank.statementprocessor.util.DocumentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.List;

@Service
public class XMLStatementProcessor implements TransactionStatementProcessor {

    private static JAXBContext jaxbContext;
    private static Unmarshaller unMarshaller;

    static {
        try {
            jaxbContext = JAXBContext.newInstance(Transactions.class);
            unMarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("Failed initialize jaxb context");
        }
    }

    @Override
    public List<Transaction> process(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Transactions transactions = (Transactions) unMarshaller.unmarshal(inputStream);
            return transactions.getTransactions();
        } catch (Exception ex) {
            throw new XMLParseException("Error processing xml document", ex, file.getOriginalFilename());
        }
    }

    @Override
    public DocumentType type() {
        return DocumentType.XML;
    }
}
