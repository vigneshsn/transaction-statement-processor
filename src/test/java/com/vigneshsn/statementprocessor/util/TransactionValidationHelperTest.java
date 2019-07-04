package com.vigneshsn.statementprocessor.util;

import com.vigneshsn.statementprocessor.models.Transaction;
import org.junit.Test;

import java.util.List;

import static com.vigneshsn.statementprocessor.TestHelper.prepareTransactionList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionValidationHelperTest {

    @Test
    public void test_duplicate_transactions() {
        List<Transaction> duplicateTransactions =
                TransactionValidationHelper.getDuplicateTransactions(prepareTransactionList());
        assertTrue("should contains duplicate transactions", duplicateTransactions.size() > 0);
        assertEquals(duplicateTransactions.get(0).getId(), "171149");
    }

    @Test
    public void test_incorrect_transactions() {
        List<Transaction> incorrectTransactions =
                TransactionValidationHelper.getIncorrectTransactions(prepareTransactionList());
        assertTrue("should contains incorrect transactions", incorrectTransactions.size() > 0);
        assertEquals(incorrectTransactions.get(0).getId(), "158536");
    }
}
