package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.dto.AuditLogDTO;
import com.telemedicine.telemedicine_backend.entity.AdminAuditLog;
import com.telemedicine.telemedicine_backend.repository.AdminAuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    private final AdminAuditLogRepository auditLogRepository;

    public AuditLogService(AdminAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String adminUsername, String action, String targetType, Long targetId, String details) {
        AdminAuditLog log = new AdminAuditLog(adminUsername, action, targetType, targetId, details);
        auditLogRepository.save(log);
    }

    public List<AuditLogDTO> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getAuditLogsByAdmin(String adminUsername) {
        return auditLogRepository.findByAdminUsername(adminUsername).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getAuditLogsByAction(String action) {
        return auditLogRepository.findByAction(action).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getAuditLogsBetweenDates(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTimestampBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuditLogDTO convertToDTO(AdminAuditLog log) {
        return new AuditLogDTO(
                log.getId(),
                log.getAdminUsername(),
                log.getAction(),
                log.getTargetType(),
                log.getTargetId(),
                log.getDetails(),
                log.getTimestamp()
        );
    }
}
