package com.vigneshsn.statementprocessor.CSVProcessor;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.vigneshsn.statementprocessor.exceptions.CSVParseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class CSVHelper {

    public static <T> List<T> parse(MultipartFile file, Class<T> type) {
        try (Reader fileReader = new InputStreamReader(file.getInputStream())) {
            HeaderColumnNameMappingStrategy mappingStrategy = new HeaderColumnNameMappingStrategy();
            mappingStrategy.setType(type);

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
