package ru.Bank.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.Bank.Entity.Account;
import ru.Bank.Entity.OperationLog;
import ru.Bank.Entity.User;

import java.util.List;


public interface OperationLogRepository extends CrudRepository<OperationLog, Integer> {
    List<OperationLog> findAllByUser(User user);
    List<OperationLog> findAllByFromAccountOrToAccount(Account fromAccount, Account toAccount);
}
