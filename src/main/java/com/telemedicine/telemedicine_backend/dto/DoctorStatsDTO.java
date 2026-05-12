package com.telemedicine.telemedicine_backend.dto;

import java.util.Map;

public class DoctorStatsDTO {
    private long totalDoctors;
    private long totalSpecializations;
    private int totalAvailableSlots;
    private Map<String, Long> doctorsBySpecialization;

    public DoctorStatsDTO() {}

    public DoctorStatsDTO(long totalDoctors, long totalSpecializations, int totalAvailableSlots,
                          Map<String, Long> doctorsBySpecialization) {
        this.totalDoctors = totalDoctors;
        this.totalSpecializations = totalSpecializations;
        this.totalAvailableSlots = totalAvailableSlots;
        this.doctorsBySpecialization = doctorsBySpecialization;
    }

    public long getTotalDoctors() {
        return totalDoctors;
    }

    public void setTotalDoctors(long totalDoctors) {
        this.totalDoctors = totalDoctors;
    }

    public long getTotalSpecializations() {
        return totalSpecializations;
    }

    public void setTotalSpecializations(long totalSpecializations) {
        this.totalSpecializations = totalSpecializations;
    }

    public int getTotalAvailableSlots() {
        return totalAvailableSlots;
    }

    public void setTotalAvailableSlots(int totalAvailableSlots) {
        this.totalAvailableSlots = totalAvailableSlots;
    }

    public Map<String, Long> getDoctorsBySpecialization() {
        return doctorsBySpecialization;
    }

    public void setDoctorsBySpecialization(Map<String, Long> doctorsBySpecialization) {
        this.doctorsBySpecialization = doctorsBySpecialization;
    }
}
