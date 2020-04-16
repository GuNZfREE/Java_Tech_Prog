package ru.Bank.Service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Bank.Entity.User;
import ru.Bank.Repository.UserRepository;

import java.util.List;

@Service
public class UserService extends AbstractService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
