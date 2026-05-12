package com.telemedicine.telemedicine_backend.dto;

import java.time.LocalDateTime;

public class AuditLogDTO {
    private Long id;
    private String adminUsername;
    private String action;
    private String targetType;
    private Long targetId;
    private String details;
    private LocalDateTime timestamp;

    public AuditLogDTO() {}

    public AuditLogDTO(Long id, String adminUsername, String action, String targetType,
                       Long targetId, String details, LocalDateTime timestamp) {
        this.id = id;
        this.adminUsername = adminUsername;
        this.action = action;
        this.targetType = targetType;
        this.targetId = targetId;
        this.details = details;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdminUsername() { return adminUsername; }
    public void setAdminUsername(String adminUsername) { this.adminUsername = adminUsername; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
