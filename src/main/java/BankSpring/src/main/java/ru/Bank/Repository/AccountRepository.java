package ru.Bank.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.Bank.Entity.Account;
import ru.Bank.Entity.User;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends CrudRepository<Account, UUID> {
    List<Account> findAllByUserAndDeletedAtIsNull(User user);
    Account findAccountByIdAndDeletedAtIsNull(UUID uuid);
}
