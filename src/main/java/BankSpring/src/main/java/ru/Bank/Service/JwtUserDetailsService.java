package ru.Bank.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.Bank.Entity.User;
import ru.Bank.Repository.UserRepository;

import java.util.ArrayList;

@Component
public class JwtUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(name);
        if (user == null) {
            user = userRepository.findByPhone(name);
            if (user == null) {
                return null;
            }
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                new ArrayList<>());
    }

}
