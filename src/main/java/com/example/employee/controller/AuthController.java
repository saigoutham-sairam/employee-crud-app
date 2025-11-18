package com.example.employee.controller;

import com.example.employee.model.*;
import com.example.employee.service.MfaService;
import com.example.employee.service.NotificationService;
import com.example.employee.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final MfaService mfaService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public AuthController(MfaService mfaService, UserService userService, NotificationService notificationService) {
        this.mfaService = mfaService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @PostMapping("/login")
    private ResponseEntity<LoginResponse> login(
            @Valid
            @NotNull
            @RequestBody LoginRequest loginRequest
    ) {
        Optional<User> user = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        LoginResponse loginResponse = new LoginResponse();
        if (user.isPresent()) {
            User loggedUser = user.get();
            String sessionId = mfaService.getActiveMfaSessionId(loginRequest.getUsername());
            String otp = mfaService.generateOTP(loggedUser.getUsername());
            notificationService.sendOtp(loggedUser,otp);
            loginResponse.setSessionId(sessionId);
            loginResponse.setMessage("Login successful");
            return ResponseEntity.ok(loginResponse);
        }
        loginResponse.setMessage("Invalid User");
        loginResponse.setSessionId(null);
        return ResponseEntity.status(401).body(loginResponse);
    }

    // Step 2: OTP verification
    @PostMapping("/verify-otp")
    public ResponseEntity<OtpResponse> verifyOtp(@RequestBody OtpRequest request) {
        boolean isValid = mfaService.validateOtp(request.getSessionId(), request.getOtp());

        OtpResponse response = new OtpResponse();
        response.setVerified(isValid);
        response.setMessage(isValid ? "OTP verified successfully!" : "OTP verification failed");

        return ResponseEntity.ok(response);
    }
}
