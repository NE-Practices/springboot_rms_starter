package com.spring.rms.repositories;

import com.spring.rms.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IAuditLogRepository extends JpaRepository<AuditLog, UUID> {
}