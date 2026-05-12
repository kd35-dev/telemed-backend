package com.telemedicine.telemedicine_backend.dto;

import java.time.LocalDate;
import java.util.List;

public class DoctorAvailabilityDTO {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private LocalDate availableDate;
    private int totalSlots;
    private int bookedCount;
    private int availableSlots;
    private List<String> slotTimes; // Exact times allocated by admin

    public DoctorAvailabilityDTO() {}

    public DoctorAvailabilityDTO(Long id, Long doctorId, String doctorName, LocalDate availableDate,
                                 int totalSlots, int bookedCount, List<String> slotTimes) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.availableDate = availableDate;
        this.totalSlots = totalSlots;
        this.bookedCount = bookedCount;
        this.availableSlots = totalSlots - bookedCount;
        this.slotTimes = slotTimes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public LocalDate getAvailableDate() { return availableDate; }
    public void setAvailableDate(LocalDate availableDate) { this.availableDate = availableDate; }

    public int getTotalSlots() { return totalSlots; }
    public void setTotalSlots(int totalSlots) { this.totalSlots = totalSlots; }

    public int getBookedCount() { return bookedCount; }
    public void setBookedCount(int bookedCount) { this.bookedCount = bookedCount; }

    public int getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(int availableSlots) { this.availableSlots = availableSlots; }

    public List<String> getSlotTimes() { return slotTimes; }
    public void setSlotTimes(List<String> slotTimes) { this.slotTimes = slotTimes; }
}
