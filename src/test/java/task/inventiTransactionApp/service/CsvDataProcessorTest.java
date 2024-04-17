package task.inventiTransactionApp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import task.inventiTransactionApp.entities.Transactions;
import task.inventiTransactionApp.repo.TransactionsRepo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CsvDataProcessorTest {

    @Mock
    private TransactionsRepo transactionsRepo;

    @InjectMocks
    private CsvDataProcessor csvDataProcessor;

    private final String accountNumber1 = "123456789";
    private final Timestamp timestamp1 = Timestamp.valueOf("2024-06-01 12:00:00.0");
    private final String beneficiary1 = "Beneficiary1";
    private final String comment1 = "comment1";
    private final BigDecimal amount1 = BigDecimal.TEN;
    private final BigDecimal result1 = new BigDecimal("9.40");
    private final String currency1 = "USD";

    @Test
    void testGenerateCsv() {
        Date startDate = new Date();
        Date endDate = new Date();
        List<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, amount1, currency1));

        when(transactionsRepo.findByOperationTimeBetween(startDate, endDate)).thenReturn(transactionsList);

        StringBuilder result = csvDataProcessor.generateCsv(startDate, endDate);

        String expectedCsvContent = "123456789,2024-06-01 12:00:00.0,Beneficiary1,comment1,10,USD";
        assertEquals(expectedCsvContent, result.toString());
    }

    @Test
    void testImportCsv() throws IOException {
        String csvContent = "123456789,2024-06-01 12:00:00.0,Beneficiary1,comment1,10,USD";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        MultipartFile multipartFile = new MockMultipartFile("file.csv", inputStream);

        when(transactionsRepo.existsByAccountNumberAndOperationTimeAndBeneficiary(any(), any(), any())).thenReturn(false);

        csvDataProcessor.importCvs(multipartFile);

        verify(transactionsRepo, times(1)).saveAll(any());
    }

    @Test
    void testProcessCsvDataToList() {
        String[] rowData = {accountNumber1, timestamp1.toString(), beneficiary1, comment1, amount1.toString(), currency1};
        Set<String[]> csvData = new HashSet<>(Collections.singletonList(rowData));

        List<Transactions> result = csvDataProcessor.processCsvDataToList(csvData);

        assertEquals(1, result.size(), "List wrong size");
        Transactions transaction = result.getFirst();
        assertEquals(accountNumber1, transaction.getAccountNumber(), "Account number does not match");
        assertEquals(timestamp1, transaction.getOperationTime(), "Timestamp does not match");
        assertEquals(beneficiary1, transaction.getBeneficiary(), "Beneficiary does not match");
        assertEquals(comment1, transaction.getComment(), "Comment does not match");
        assertEquals(amount1, transaction.getAmount(), "Ammount does not match");
        assertEquals(currency1, transaction.getCurrency(), "Currency does not match");
    }

    @Test
    void testCalculateBalance() {
        Date startDate = new Date();
        Date endDate = new Date();
        List<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(new Transactions(accountNumber1, timestamp1, beneficiary1, comment1, amount1, currency1));

        when(transactionsRepo.findByAccountNumberAndOperationTimeBetween(accountNumber1, startDate, endDate)).thenReturn(transactionsList);

        BigDecimal result = csvDataProcessor.calculateBalance(accountNumber1, startDate, endDate);

        assertEquals(result1, result, "Result of balance calculation does not match");
    }

}