package task.inventiTransactionApp.entities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionsTest {


    @Autowired
    private TestEntityManager entityManager;

    private final String accountNumber1 = "123456789";
    private final Timestamp timestamp1 = Timestamp.valueOf("2024-06-01 12:00:00");
    private final String beneficiary1 = "Beneficiary1";
    private final String comment1 = "comment1";
    private final BigDecimal amount1 = BigDecimal.TEN;
    private final String currency1 = "USD";

    @Test
    public void testConstructorAndGetters() {
        Transactions transaction = new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, amount1, currency1);
        assertEquals(accountNumber1, transaction.getAccountNumber(), "Account number does not match");
        assertEquals(timestamp1, transaction.getOperationTime(), "Timestamp does not match");
        assertEquals(beneficiary1, transaction.getBeneficiary(), "Beneficiary does not match");
        assertEquals(comment1, transaction.getComment(), "Comment does not match");
        assertEquals(amount1, transaction.getAmount(), "Ammount does not match");
        assertEquals(currency1, transaction.getCurrency(), "Currency does not match");
    }

    @Test
    public void testSetterAndGetters() {
        Transactions transaction = new Transactions();
        transaction.setAccountNumber(accountNumber1);
        transaction.setOperationTime(timestamp1);
        transaction.setBeneficiary(beneficiary1);
        transaction.setComment(comment1);
        transaction.setAmount(amount1);
        transaction.setCurrency(currency1);

        assertEquals(accountNumber1, transaction.getAccountNumber(), "Account number does not match");
        assertEquals(timestamp1, transaction.getOperationTime(), "Timestamp does not match");
        assertEquals(beneficiary1, transaction.getBeneficiary(), "Beneficiary does not match");
        assertEquals(comment1, transaction.getComment(), "Comment does not match");
        assertEquals(amount1, transaction.getAmount(), "Ammount does not match");
        assertEquals(currency1, transaction.getCurrency(), "Currency does not match");
    }

    @Test
    public void testToString() {
        Transactions transaction = new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, amount1, currency1);
        String expectedToString = "123456789,2024-06-01 12:00:00.0,Beneficiary1,comment1,10,USD";

        assertEquals(expectedToString, transaction.toString(), "String does not match");
    }

    @Test
    public void testSaveWithValidData() {
        Transactions transaction = new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, amount1, currency1);
        Transactions savedTransaction = entityManager.persistAndFlush(transaction);
        assertEquals(transaction, savedTransaction, "Data is not valid");
    }

    @Test
    public void testSaveWithNullAccountNumber() {
        assertThrows(org.hibernate.PropertyValueException.class, () -> {
            Transactions transaction = new Transactions(null, timestamp1, beneficiary1, comment1, amount1, currency1);
            entityManager.persistAndFlush(transaction);
        }, "Account number saved with null");
    }

    @Test
    public void testSaveWithNullOperationTime() {
        assertThrows(org.hibernate.PropertyValueException.class, () -> {
            Transactions transaction = new Transactions(accountNumber1, null, beneficiary1, comment1, amount1, currency1);
            entityManager.persistAndFlush(transaction);
        }, "Operation time saved with null");
    }

    @Test
    public void testSaveWithNullBeneficiary() {
        assertThrows(org.hibernate.PropertyValueException.class, () -> {
            Transactions transaction = new Transactions(accountNumber1, timestamp1, null, comment1, amount1, currency1);
            entityManager.persistAndFlush(transaction);
        }, "Beneficiary saved with null");
    }

    @Test
    public void testSaveWithNullAmount() {
        assertThrows(org.hibernate.PropertyValueException.class, () -> {
            Transactions transaction = new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, null, currency1);
            entityManager.persistAndFlush(transaction);
        }, "Amount saved with null");
    }

    @Test
    public void testSaveWithNullCurrency() {
        assertThrows(org.hibernate.PropertyValueException.class, () -> {
            Transactions transaction = new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, amount1, null);
            entityManager.persistAndFlush(transaction);
        }, "Currency saved with null");
    }

    @Test
    public void testSaveWithInvalidAccountNumberLength() {
        assertThrows(org.hibernate.exception.DataException.class, () -> {
            Transactions transaction = new Transactions(accountNumber1 + "0", timestamp1, beneficiary1, comment1, amount1, currency1);
            entityManager.persistAndFlush(transaction);
        }, "Saved with invalid account number length");
    }

    @Test
    public void testSaveWithInvalidCurrencyLength() {
        assertThrows(org.hibernate.exception.DataException.class, () -> {
            Transactions transaction = new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, amount1, currency1 + "X");
            entityManager.persistAndFlush(transaction);
        }, "Saved with invalid currency length");
    }

}