package com.example.employee.repository;

import com.example.employee.model.MfaSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MfaSessionRepository extends JpaRepository<MfaSession, Long> {

    Optional<MfaSession> findBySessionId(String sessionId);

    Optional<MfaSession> findByUsernameAndVerifiedFalseAndExpiresAtAfter(
            String username,
            LocalDateTime currentTime
    );
    void deleteByExpiresAtBefore(LocalDateTime currentTime);
}

