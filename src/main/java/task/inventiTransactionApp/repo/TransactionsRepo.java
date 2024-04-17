package task.inventiTransactionApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task.inventiTransactionApp.entities.Transactions;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionsRepo extends JpaRepository<Transactions, Long> {

    List<Transactions> findByOperationTimeBetween(Date startDate, Date endDate);

    boolean existsByAccountNumberAndOperationTimeAndBeneficiary(
            String accountNumber, Timestamp operationTime, String beneficiary);

    List<Transactions> findByAccountNumberAndOperationTimeBetween(String accountNumber, Date startDate, Date endDate);

    List<Transactions> findAllByAccountNumber(String accountNumber);

}
