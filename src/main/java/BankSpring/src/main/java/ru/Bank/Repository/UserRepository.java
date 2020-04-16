package ru.Bank.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.Bank.Entity.User;


public interface UserRepository extends CrudRepository<User, Integer> {
    User findByPhone(String phone);
    User findByLogin(String login);
    boolean existsUserByLogin(String login);
    boolean existsUserByPhone(String phone);
}
