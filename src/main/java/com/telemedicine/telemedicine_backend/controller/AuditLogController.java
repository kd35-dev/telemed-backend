package com.telemedicine.telemedicine_backend.controller;

import com.telemedicine.telemedicine_backend.dto.ApiResponse;
import com.telemedicine.telemedicine_backend.dto.AuditLogDTO;
import com.telemedicine.telemedicine_backend.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLogDTO>>> getAllAuditLogs() {
        List<AuditLogDTO> logs = auditLogService.getAllAuditLogs();
        return ResponseEntity.ok(new ApiResponse<>(true, logs));
    }

    @GetMapping("/admin/{adminUsername}")
    public ResponseEntity<ApiResponse<List<AuditLogDTO>>> getAuditLogsByAdmin(@PathVariable String adminUsername) {
        List<AuditLogDTO> logs = auditLogService.getAuditLogsByAdmin(adminUsername);
        return ResponseEntity.ok(new ApiResponse<>(true, logs));
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<ApiResponse<List<AuditLogDTO>>> getAuditLogsByAction(@PathVariable String action) {
        List<AuditLogDTO> logs = auditLogService.getAuditLogsByAction(action);
        return ResponseEntity.ok(new ApiResponse<>(true, logs));
    }
}

