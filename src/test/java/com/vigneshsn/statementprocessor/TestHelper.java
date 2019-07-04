package com.vigneshsn.statementprocessor;

import com.vigneshsn.statementprocessor.models.Transaction;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static byte[] readFileASBytes(final String fileName) {

        try {
            Path path = Paths.get(TestHelper.class.getClassLoader()
                    .getResource(fileName).toURI());
            return Files.readAllBytes(path);

        } catch (Exception e) {
            throw new RuntimeException("Unable to read file", e);
        }

    }

    public static List<Transaction> prepareTransactionList() {

        //duplicate transaction
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setId("171149");
        transaction1.setIban("NL46ABNA0625805417");
        transaction1.setDescription("test");
        transaction1.setStartBalance(new BigDecimal(24.06));
        transaction1.setMutation(new BigDecimal(-10.37));
        transaction1.setEndBalance(new BigDecimal(13.69));

        //incorrect balance
        Transaction transaction2 = new Transaction();
        transaction2.setId("158536");
        transaction2.setIban("NL93ABNA0585619023");
        transaction2.setDescription("test");
        transaction2.setStartBalance(new BigDecimal(5429));
        transaction2.setMutation(new BigDecimal(-939));
        transaction2.setEndBalance(new BigDecimal(6368));

        Transaction transaction3 = new Transaction();
        transaction3.setId("171149");
        transaction3.setIban("NL46ABNA0625805417");
        transaction3.setDescription("test");
        transaction3.setStartBalance(new BigDecimal(24.06));
        transaction3.setMutation(new BigDecimal(-10.37));
        transaction3.setEndBalance(new BigDecimal(13.69));

        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        return transactions;
    }
}
