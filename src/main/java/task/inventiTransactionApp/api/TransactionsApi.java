package task.inventiTransactionApp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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


    @PostMapping("/import-csv")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a CSV file.");
            }
            dataProcessor.importCvs(file);

            return ResponseEntity.status(HttpStatus.CREATED).body("CSV file imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import CSV file: " + e.getMessage());
        }
    }

}
