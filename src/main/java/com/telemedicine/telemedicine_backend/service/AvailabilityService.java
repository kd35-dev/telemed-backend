package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.dto.DoctorAvailabilityDTO;
import com.telemedicine.telemedicine_backend.entity.DoctorAvailability;
import com.telemedicine.telemedicine_backend.entity.Doctors;
import com.telemedicine.telemedicine_backend.repository.DoctorAvailabilityRepository;
import com.telemedicine.telemedicine_backend.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;

    public AvailabilityService(DoctorAvailabilityRepository availabilityRepository,
                               DoctorRepository doctorRepository) {
        this.availabilityRepository = availabilityRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Allocate EXACT slot times for a doctor on a specific date.
     * Admin specifies exact times like ["09:00", "10:00", "14:00"]
     */
    @Transactional
    public DoctorAvailability allocateSlots(Long doctorId, LocalDate date, List<String> slotTimes) {
        Doctors doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        if (slotTimes == null || slotTimes.isEmpty()) {
            throw new IllegalArgumentException("At least one slot time must be provided");
        }

        // Validate time format (HH:mm)
        for (String time : slotTimes) {
            if (!time.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                throw new IllegalArgumentException("Invalid time format: " + time + ". Use HH:mm");
            }
        }

        DoctorAvailability availability = availabilityRepository
                .findByDoctorIdAndAvailableDate(doctorId, date)
                .orElse(new DoctorAvailability(doctor, date, slotTimes.size()));

        // Prevent admin from reducing slots if appointments already booked
        if (availability.getBookedCount() > slotTimes.size()) {
            throw new IllegalArgumentException("Cannot reduce slots to " + slotTimes.size() + 
                    " when " + availability.getBookedCount() + " appointments are already booked");
        }

        availability.setTotalSlots(slotTimes.size());
        availability.setSlotTimesList(slotTimes);
        return availabilityRepository.save(availability);
    }

    /**
     * Allocate EXACT slot times for a doctor across a date range.
     * Same slot times apply to each day in the range.
     */
    @Transactional
    public List<DoctorAvailability> allocateSlotsForRange(Long doctorId, LocalDate startDate, LocalDate endDate, List<String> slotTimes) {
        Doctors doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        if (slotTimes == null || slotTimes.isEmpty()) {
            throw new IllegalArgumentException("At least one slot time must be provided");
        }

        List<DoctorAvailability> result = new ArrayList<>();
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            result.add(allocateSlots(doctorId, current, slotTimes));
            current = current.plusDays(1);
        }

        return result;
    }

    /**
     * Get available slots for a doctor on a specific date.
     * Returns ONLY the exact slot times allocated by admin, minus already booked times.
     */
    public List<String> getAvailableSlotsForDate(Long doctorId, LocalDate date) {
        DoctorAvailability availability = availabilityRepository
                .findByDoctorIdAndAvailableDate(doctorId, date)
                .orElse(null);

        if (availability == null || availability.getSlotTimes() == null || availability.getSlotTimes().isBlank()) {
            return new ArrayList<>();
        }

        return availability.getSlotTimesList();
    }

    /**
     * Get remaining slots for a doctor on a specific date.
     */

    public int getRemainingSlots(Long doctorId, LocalDate date) {
        DoctorAvailability availability = availabilityRepository
                .findByDoctorIdAndAvailableDate(doctorId, date)
                .orElse(null);

        if (availability == null) {
            return 0;
        }

        return availability.getAvailableSlots();
    }

    /**
     * Sum remaining appointment capacity for a doctor across all days in [startDate, endDate]
     * that have availability rows (matches patient slot picker window totals).
     */
    public int sumAvailableSlotsInRange(Long doctorId, LocalDate startDate, LocalDate endDate) {
        return availabilityRepository
                .findByDoctorIdAndAvailableDateBetween(doctorId, startDate, endDate)
                .stream()
                .mapToInt(DoctorAvailability::getAvailableSlots)
                .sum();
    }

    /**
     * Get doctor's availability for a date range.
     */
    public List<DoctorAvailabilityDTO> getDoctorAvailability(Long doctorId, LocalDate startDate, LocalDate endDate) {
        return availabilityRepository
                .findByDoctorIdAndAvailableDateBetween(doctorId, startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert DoctorAvailability entity to DTO.
     */
    private DoctorAvailabilityDTO convertToDTO(DoctorAvailability availability) {
        return new DoctorAvailabilityDTO(
                availability.getId(),
                availability.getDoctor().getId(),
                availability.getDoctor().getName(),
                availability.getAvailableDate(),
                availability.getTotalSlots(),
                availability.getBookedCount(),
                availability.getSlotTimesList()
        );
    }

    /**
     * Delete a doctor availability slot by ID.
     */
    @Transactional
    public void deleteAvailability(Long id) {
        DoctorAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Availability slot not found"));
        
        // Prevent deletion if there are booked appointments
        if (availability.getBookedCount() > 0) {
            throw new IllegalArgumentException("Cannot delete availability slot with " + 
                    availability.getBookedCount() + " booked appointments");
        }
        
        availabilityRepository.deleteById(id);
    }
}
