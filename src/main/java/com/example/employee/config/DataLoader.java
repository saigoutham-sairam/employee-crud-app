package com.example.employee.config;

import com.example.employee.model.User;
import com.example.employee.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("========== DataLoader started ==========");

        try {
            long userCount = userRepository.count();
            log.info("Current user count: {}", userCount);

            if (userCount == 0) {
                log.info("Loading test users into database...");

                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .email("admin@natwest.com")
                        .phoneNumber("+44 7700 900000")
                        .mfaEnabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                log.info("Saving admin user: {}", admin.getUsername());
                userRepository.save(admin);
                log.info("✓ Admin user saved successfully!");

                User user = User.builder()
                        .username("john.doe")
                        .password(passwordEncoder.encode("password123"))
                        .email("john.doe@natwest.com")
                        .phoneNumber("+44 7700 900001")
                        .mfaEnabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                log.info("Saving john.doe user: {}", user.getUsername());
                userRepository.save(user);
                log.info("✓ john.doe user saved successfully!");

                long newCount = userRepository.count();
                log.info("Total users after loading: {}", newCount);
                log.info("Test users loaded successfully!");

            } else {
                log.info("Users already exist ({} users), skipping data loading", userCount);
            }
        } catch (Exception e) {
            log.error("ERROR in DataLoader: {}", e.getMessage());
        }

        log.info("========== DataLoader finished ==========");
    }
}
