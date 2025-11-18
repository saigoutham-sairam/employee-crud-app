package com.example.employee.service;

import com.example.employee.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendOtpViaSms(User user, String otp) {
        log.info("SMS NOTIFICATION (SIMULATED)");
        log.info("To: {}", user.getPhoneNumber());
        log.info("Message: Your OTP is: {}", otp);
        log.info("This OTP is valid for 5 minutes.");
    }

    public void sendOtpViaEmail(User user, String otp) {
        log.info("EMAIL NOTIFICATION (SIMULATED)");
        log.info("To: {}", user.getEmail());
        log.info("Subject: Your OTP for login");
        log.info("Body: Your OTP is: {}", otp);
        log.info("This OTP is valid for 5 minutes.");
    }

    public void sendOtp(User user, String otp) {
        // For now, just send via SMS. Later this can be switched to Email or configurable
        sendOtpViaSms(user, otp);
    }
}

