package com.example.employee.repository;

import com.example.employee.model.MfaSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MfaSessionRepository extends JpaRepository<MfaSession,Long> {
}
