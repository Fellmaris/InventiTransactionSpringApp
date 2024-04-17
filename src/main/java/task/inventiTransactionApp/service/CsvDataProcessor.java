package task.inventiTransactionApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.inventiTransactionApp.entities.Transactions;
import task.inventiTransactionApp.repo.TransactionsRepo;

import java.util.Date;
import java.util.List;

@Service
public class CsvDataProcessor {

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
}
