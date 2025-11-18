package com.example.employee.service;

import com.example.employee.model.MfaSession;
import com.example.employee.repository.MfaSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class MfaService {

    @Autowired
    private MfaSessionRepository mfaSessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int MAX_OTP_ATTEMPTS = 3;
    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 5;

    /**
     * Generate OTP and create MFA session
     *
     * @param userName User attempting to authenticate
     * @return OTP code (plain text) to be sent to user
     */
    public String generateOTP(String userName) {
        log.info("Generating OTP for user: {}", userName);

        String otp = generateSixDigitRandom();
        log.debug("Generated OTP: {}", otp);

        String hashedOTP = passwordEncoder.encode(otp);

        MfaSession mfaSession = MfaSession.builder()
                .sessionId(UUID.randomUUID().toString())
                .username(userName)
                .attemptCount(0)
                .verified(false)
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES))
                .otpCode(hashedOTP)
                .build();

        MfaSession savedSession = mfaSessionRepository.save(mfaSession);
        log.info("✓ OTP generated and MFA session created: sessionId={}", savedSession.getSessionId());

        return otp;  // Return plain OTP to send to user
    }

    /**
     * Get MFA session ID for user (to return to client)
     *
     * @param userName User's username
     * @return Session ID if active session exists, null otherwise
     */
    public String getActiveMfaSessionId(String userName) {
        try {
            Optional<MfaSession> session = mfaSessionRepository
                    .findByUsernameAndVerifiedFalseAndExpiresAtAfter(userName, LocalDateTime.now());

            if (session.isPresent()) {
                return session.get().getSessionId();
            }
        } catch (Exception e) {
            log.error("Error finding active MFA session for user: {}", userName, e);
        }
        return null;
    }

    /**
     * Validate OTP submitted by user
     *
     * @param mfaSessionId MFA session ID
     * @param otp OTP submitted by user (plain text)
     * @return true if OTP is valid, false otherwise
     */
    public boolean validateOtp(String mfaSessionId, String otp) {
        log.info("Validating OTP for session: {}", mfaSessionId);

        try {
            // Find MFA session by session ID
            Optional<MfaSession> mfaSession = mfaSessionRepository.findBySessionId(mfaSessionId);

            if (mfaSession.isEmpty()) {
                log.warn("MFA session not found: {}", mfaSessionId);
                return false;
            }

            MfaSession existingSession = mfaSession.get();

            // Check if maximum attempts exceeded
            if (existingSession.getAttemptCount() >= MAX_OTP_ATTEMPTS) {
                log.error("✗ Maximum OTP attempts exceeded for session: {}", mfaSessionId);
                return false;
            }

            // Check if OTP has expired
            if (existingSession.getExpiresAt().isBefore(LocalDateTime.now())) {
                log.error("✗ OTP has expired for session: {}", mfaSessionId);
                return false;
            }

            // Check if session already verified
            if (existingSession.isVerified()) {
                log.warn("Session already verified: {}", mfaSessionId);
                return false;
            }

            // passwordEncoder.matches(plainText, hashed)
            if (passwordEncoder.matches(otp, existingSession.getOtpCode())) {
                // OTP is correct!
                existingSession.setVerified(true);
                mfaSessionRepository.save(existingSession);

                log.info("✓ OTP validation successful for session: {}", mfaSessionId);
                return true;
            } else {
                // OTP is incorrect, increment attempt count
                existingSession.setAttemptCount(existingSession.getAttemptCount() + 1);
                mfaSessionRepository.save(existingSession);

                int remainingAttempts = MAX_OTP_ATTEMPTS - existingSession.getAttemptCount();
                log.warn("✗ Invalid OTP. Attempt {}/{} for session: {}",
                        existingSession.getAttemptCount(), MAX_OTP_ATTEMPTS, mfaSessionId);

                if (remainingAttempts > 0) {
                    log.warn("  Remaining attempts: {}", remainingAttempts);
                }

                return false;
            }

        } catch (Exception e) {
            log.error("Error validating OTP for session: {}", mfaSessionId, e);
            return false;
        }
    }

    /**
     * Check if MFA session is verified
     *
     * @param mfaSessionId MFA session ID
     * @return true if verified, false otherwise
     */
    public boolean isMfaSessionVerified(String mfaSessionId) {
        try {
            Optional<MfaSession> session = mfaSessionRepository.findBySessionId(mfaSessionId);
            return session.isPresent() && session.get().isVerified();
        } catch (Exception e) {
            log.error("Error checking MFA session verification for: {}", mfaSessionId, e);
            return false;
        }
    }

    /**
     * Generate random 6-digit OTP
     *
     * @return Random 6-digit string (000000-999999)
     */
    private String generateSixDigitRandom() {
        Random random = new Random();
        int otp = random.nextInt((int) Math.pow(10, OTP_LENGTH));
        return String.format("%0" + OTP_LENGTH + "d", otp);
    }
}

