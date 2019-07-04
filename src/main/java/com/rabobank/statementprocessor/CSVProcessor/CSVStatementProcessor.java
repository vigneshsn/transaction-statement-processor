package com.rabobank.statementprocessor.CSVProcessor;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.rabobank.statementprocessor.api.TransactionStatementProcessor;
import com.rabobank.statementprocessor.exceptions.CSVParseException;
import com.rabobank.statementprocessor.models.Transaction;
import com.rabobank.statementprocessor.util.DocumentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class CSVStatementProcessor implements TransactionStatementProcessor {

    private static HeaderColumnNameMappingStrategy mappingStrategy;

    static {
        mappingStrategy = new HeaderColumnNameMappingStrategy();
        mappingStrategy.setType(Transaction.class);
    }

    @Override
    public List<Transaction> process(final MultipartFile file) {
        return parse(file, Transaction.class);
    }

    @Override
    public DocumentType type() {
        return DocumentType.CSV;
    }

    private <T> List<T> parse(final MultipartFile file, Class<T> type) {
        try (InputStream fileInputStream = file.getInputStream()) {
            Reader fileReader = new InputStreamReader(fileInputStream);
            CsvToBean csvToBean = new CsvToBeanBuilder(fileReader)
                    .withType(type)
                    .withMappingStrategy(mappingStrategy)
                    .build();
            return csvToBean.parse();
        } catch (Exception ex) {
            throw new CSVParseException("File processor exception", ex, file.getOriginalFilename());
        }
    }

}
