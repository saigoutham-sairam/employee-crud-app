package com.example.employee.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mfa_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MfaSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", unique = true, nullable = false)
    private String sessionId;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "otp_code", nullable = false)
    private String otpCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private String verified;

    @Column(name = "attempt_count")
    private int attemptCount = 0;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        expiresAt = createdAt.plusMinutes(5);
    }
}
