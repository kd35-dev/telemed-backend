package com.telemedicine.telemedicine_backend.dto;

import java.time.LocalDate;
import java.util.List;

public class SlotAllocationRequestDTO {
    private Long doctorId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> slotTimes; // Exact times: ["09:00", "09:30", "10:00", ...]

    public SlotAllocationRequestDTO() {}

    public SlotAllocationRequestDTO(Long doctorId, LocalDate startDate, LocalDate endDate, List<String> slotTimes) {
        this.doctorId = doctorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.slotTimes = slotTimes;
    }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public List<String> getSlotTimes() { return slotTimes; }
    public void setSlotTimes(List<String> slotTimes) { this.slotTimes = slotTimes; }
}
