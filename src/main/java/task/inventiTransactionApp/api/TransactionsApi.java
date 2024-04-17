package task.inventiTransactionApp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import task.inventiTransactionApp.service.CsvDataProcessor;

import java.util.Date;

public class TransactionsApi {

    @Autowired
    private CsvDataProcessor dataProcessor;

    @GetMapping("/generate-csv")
    public ResponseEntity<String> generateCsv(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        StringBuilder csvContent = dataProcessor.generateCsv(startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ExportData.txt");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent.toString());
    }
}
