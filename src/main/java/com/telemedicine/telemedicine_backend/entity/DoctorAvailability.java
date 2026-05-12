package com.telemedicine.telemedicine_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(
    name = "doctor_availability",
    uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "available_date"})
)
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctors doctor;

    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;

    @Column(nullable = false)
    private int totalSlots;

    @Column(nullable = false)
    private int bookedCount = 0;

    @Column(name = "slot_times", columnDefinition = "TEXT")
    private String slotTimes; // JSON array: ["09:00", "09:30", "10:00", ...]

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public DoctorAvailability() {}

    public DoctorAvailability(Doctors doctor, LocalDate availableDate, int totalSlots) {
        this.doctor = doctor;
        this.availableDate = availableDate;
        this.totalSlots = totalSlots;
        this.bookedCount = 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctors getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctors doctor) {
        this.doctor = doctor;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public int getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(int bookedCount) {
        this.bookedCount = bookedCount;
    }

    public int getAvailableSlots() {
        return totalSlots - bookedCount;
    }

    public String getSlotTimes() {
        return slotTimes;
    }

    public void setSlotTimes(String slotTimes) {
        this.slotTimes = slotTimes;
    }

    public List<String> getSlotTimesList() {
        if (slotTimes == null || slotTimes.isBlank()) {
            return new ArrayList<>();
        }
        try {
            // Parse JSON array string: ["09:00","10:00","11:00"] -> [09:00, 10:00, 11:00]
            String cleaned = slotTimes.trim();
            if (cleaned.startsWith("[") && cleaned.endsWith("]")) {
                cleaned = cleaned.substring(1, cleaned.length() - 1);
            }
            if (cleaned.isEmpty()) {
                return new ArrayList<>();
            }
            return Arrays.stream(cleaned.split(","))
                    .map(s -> s.trim().replaceAll("\"", ""))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void setSlotTimesList(List<String> times) {
        if (times == null || times.isEmpty()) {
            this.slotTimes = "[]";
        } else {
            // Convert list to JSON array string: [09:00, 10:00] -> ["09:00","10:00"]
            String jsonArray = "[" + times.stream()
                    .map(t -> "\"" + t + "\"")
                    .collect(Collectors.joining(",")) + "]";
            this.slotTimes = jsonArray;
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
