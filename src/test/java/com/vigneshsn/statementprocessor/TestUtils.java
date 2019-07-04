package com.vigneshsn.statementprocessor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {

    public static byte[] readFileASBytes(final String fileName) {

        try {
            Path path = Paths.get(TestUtils.class.getClassLoader()
                    .getResource(fileName).toURI());
            return Files.readAllBytes(path);

        } catch (Exception e) {
            throw new RuntimeException("Unable to read file", e);
        }

    }
}
