package task.inventiTransactionApp.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import task.inventiTransactionApp.service.CsvDataProcessor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionsApiTest {

    private final String accountNumber1 = "123456789";
    private final BigDecimal amount1 = BigDecimal.TEN;

    @Mock
    private CsvDataProcessor dataProcessor;

    @InjectMocks
    private TransactionsApi transactionsApi;

    @Test
    void testGenerateCsv() {
        Date startDate = new Date();
        Date endDate = new Date();
        StringBuilder csvContent = new StringBuilder("123456789,2024-06-01 12:00:00,Beneficiary1,comment1,10,USD\n");

        when(dataProcessor.generateCsv(startDate, endDate)).thenReturn(csvContent);

        ResponseEntity<String> response = transactionsApi.generateCsv(startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "HttpStatus does not match");
        assertEquals("text/csv", response.getHeaders().getContentType().toString(), "Content type does not match");
        assertEquals("attachment; filename=ExportData.txt",
                response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).getFirst(), "Header does not match");
        assertEquals(csvContent.toString(), response.getBody(), "File content does not match");
    }

    @Test
    void testImportCsv() throws IOException {
        ClassPathResource resource = new ClassPathResource("testFiles/ImportTest.txt");
        byte[] fileContent = StreamUtils.copyToByteArray(resource.getInputStream());

        MultipartFile file = new MockMultipartFile("ImportTest.txt", "ImportTest.txt", "text/csv", fileContent);

        transactionsApi.importCsv(file);

        verify(dataProcessor).importCvs(file);
    }

    @Test
    void testCalculateBalance() {
        Date startDate = new Date();
        Date endDate = new Date();
        BigDecimal balance = amount1;

        when(dataProcessor.calculateBalance(accountNumber1, startDate, endDate)).thenReturn(balance);

        ResponseEntity<String> response = transactionsApi.calculateBalance(accountNumber1, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "HttpStatus does not match");
        assertEquals("Balance for " + accountNumber1 + " is: " + balance + "EUR", response.getBody(), "Result does not match");
    }
}