package task.inventiTransactionApp.repo;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import task.inventiTransactionApp.entities.Transactions;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionsRepoTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TransactionsRepo transactionsRepo;

    private final String accountNumber1 = "123456789";
    private final String accountNumber2 = "123456789";
    private final Timestamp timestamp1 = Timestamp.valueOf("2024-06-01 12:00:00");
    private final Timestamp timestamp2 = Timestamp.valueOf("2024-07-01 12:00:00");
    private final String beneficiary1 = "Beneficiary1";
    private final String beneficiary2 = "Beneficiary2";
    private final String comment1 = "comment1";
    private final String comment2 = "comment2";
    private final BigDecimal amount1 = BigDecimal.TEN;
    private final BigDecimal amount2 = BigDecimal.valueOf(20);
    private final String currency1 = "USD";
    private final String currency2 ="EUR";


    @Test
    public void testFindByOperationTimeBetween() {
        Date startDate = Timestamp.valueOf("2024-01-01 00:00:00");
        Date endDate = Timestamp.valueOf("2024-06-31 23:59:59");
        Transactions transaction1 = new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, amount1, currency1);
        Transactions transaction2 = new Transactions(accountNumber2, timestamp2, beneficiary2, comment2, amount2, currency2);
        entityManager.persist(transaction1);
        entityManager.persist(transaction2);
        entityManager.flush();

        List<Transactions> result = transactionsRepo.findByOperationTimeBetween(startDate, endDate);

        assertEquals(2, result.size(), "Amount of entries in list does not match");
        assertTrue(result.contains(transaction1), "Does not contain transaction1");
        assertTrue(result.contains(transaction2), "Does not contain transaction2");
    }

    @Test
    public void testExistsByAccountNumberAndOperationTimeAndBeneficiary() {
        Transactions transaction = new Transactions(accountNumber1, timestamp2, beneficiary1, comment1, amount1, currency1);
        entityManager.persist(transaction);
        entityManager.flush();

        boolean exists = transactionsRepo.existsByAccountNumberAndOperationTimeAndBeneficiary(accountNumber1, timestamp1, beneficiary1);

        assertTrue(exists, "Transaction not found");
    }

    @Test
    public void testFindByAccountNumberAndOperationTimeBetween() {
        Date startDate = Timestamp.valueOf("2024-01-01 00:00:00");
        Date endDate = Timestamp.valueOf("2024-12-31 23:59:59");
        Transactions transaction1 = new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, amount1, currency1);
        Transactions transaction2 = new Transactions(accountNumber1, timestamp2, beneficiary2, comment1, amount2, currency2);
        entityManager.persist(transaction1);
        entityManager.persist(transaction2);
        entityManager.flush();

        List<Transactions> result = transactionsRepo.findByAccountNumberAndOperationTimeBetween(accountNumber1, startDate, endDate);

        assertEquals(2, result.size(), "Result list does not match");
        assertTrue(result.contains(transaction1), "List does not contain transaction1");
        assertTrue(result.contains(transaction2), "List does not contain transaction2");
    }

    @Test
    public void testFindAllByAccountNumber() {
        Transactions transaction1 = new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, amount1, currency1);
        Transactions transaction2 = new Transactions(accountNumber1, timestamp2, beneficiary2, comment1, amount2, currency2);
        entityManager.persist(transaction1);
        entityManager.persist(transaction2);
        entityManager.flush();

        List<Transactions> result = transactionsRepo.findAllByAccountNumber(accountNumber1);

        assertEquals(2, result.size(), "Result list does not match");
        assertTrue(result.contains(transaction1), "List does not contain transaction1");
        assertTrue(result.contains(transaction2), "List does not contain transaction2");
    }

}