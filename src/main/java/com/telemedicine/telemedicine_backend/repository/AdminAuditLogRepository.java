package com.telemedicine.telemedicine_backend.repository;

import com.telemedicine.telemedicine_backend.entity.AdminAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, Long> {
    List<AdminAuditLog> findByAdminUsername(String adminUsername);
    List<AdminAuditLog> findByAction(String action);
    List<AdminAuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
