package com.telemedicine.telemedicine_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "symptoms_logs")
public class SymptomLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String symptom;

    @Column(name = "ai_condition", nullable = false, length = 1000)
    private String condition;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public SymptomLog() {}
    public SymptomLog(String symptom, String condition, String severity, String specialization, LocalDateTime createdAt) {
        this.symptom = symptom;
        this.condition = condition;
        this.severity = severity;
        this.specialization = specialization;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
