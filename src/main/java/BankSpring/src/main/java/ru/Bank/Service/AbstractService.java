package ru.Bank.Service;


import org.springframework.beans.factory.annotation.Autowired;
import ru.Bank.Entity.User;
import ru.Bank.Repository.UserRepository;

public class AbstractService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(String username) {
        User user = userRepository.findByLogin(username);
        if (user == null)
            return userRepository.findByPhone(username);
        return user;
    }
}
