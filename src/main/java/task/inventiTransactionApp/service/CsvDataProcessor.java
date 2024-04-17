package task.inventiTransactionApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import task.inventiTransactionApp.entities.Transactions;
import task.inventiTransactionApp.repo.TransactionsRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CsvDataProcessor {

    private static final BigDecimal eurInUSD = new BigDecimal("0.94");
    private static final BigDecimal eurInGBP = new BigDecimal("1.17");
    private static final BigDecimal eurInCAD = new BigDecimal("0.68");

    @Autowired
    private TransactionsRepo transactionsRepo;

    public StringBuilder generateCsv (Date startDate, Date endDate){
        StringBuilder csvContent = new StringBuilder();
        List<Transactions> selectedTransactions = startDate != null && endDate != null
                ? transactionsRepo.findByOperationTimeBetween(startDate, endDate) : transactionsRepo.findAll();
        for (Transactions row : selectedTransactions) {
            csvContent.append(row.toString());
        }
        return csvContent;
    }

    public void importCvs(MultipartFile file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        Set<String[]> csvData = new LinkedHashSet<>();
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            if(!transactionsRepo.existsByAccountNumberAndOperationTimeAndBeneficiary(values[0], Timestamp.valueOf(values[1]), values[2])) {
                csvData.add(values);
            }
        }
        transactionsRepo.saveAll(processCsvDataToList(csvData));
    }

    public List<Transactions> processCsvDataToList(Set<String[]> csvData){
        List<Transactions> list = new ArrayList<>();
        for (String[] row: csvData) {
            Transactions entity = new Transactions();
            entity.setAccounNumber(row[0]);
            entity.setOperationTime(Timestamp.valueOf(row[1]));
            entity.setBeneficiary(row[2]);
            entity.setComment(row[3]);
            entity.setAmount(new BigDecimal(row[4]));
            entity.setCurrency(row[5]);
            list.add(entity);
        }
        return list;
    }

    public BigDecimal calculateBalance (String accountNumber, Date startDate, Date endDate){
        BigDecimal accountBalance = BigDecimal.ZERO;
        List<Transactions> data = startDate != null && endDate != null
                ? transactionsRepo.findByAccountNumberAndOperationTimeBetween(accountNumber, startDate, endDate)
                : transactionsRepo.findAllByAccountNumber(accountNumber);
        for (Transactions entity : data) {
            BigDecimal exchangeRate = switch (entity.getCurrency()){
                case "GBP" -> eurInGBP;
                case "USD" -> eurInUSD;
                case "CAD" -> eurInCAD;
                case "EUR" -> BigDecimal.ONE;
                default -> BigDecimal.ZERO;
            };
            accountBalance = accountBalance.add(entity.getAmount().multiply(exchangeRate));
        }
        return accountBalance.setScale(2, RoundingMode.HALF_DOWN);
    }

}
