package com.example.employee.service;

import com.example.employee.model.User;
import com.example.employee.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional<User> authenticateUser(String userName, String password) {
        try {
            Optional<User> user = Optional.ofNullable(userRepository.findByUsername(userName));
            if (user.isPresent()) {
                User foundUser = user.get();
                if (foundUser.isMfaEnabled() && (bCryptPasswordEncoder.matches(password, foundUser.getPassword()))) {
                    return Optional.of(foundUser);
                }
            } else {
                log.error("User : {} not found!" , userName);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error while handling user authentication for User : {}" , userName);
            return Optional.empty();
        }
    }

}
