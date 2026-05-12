package com.telemedicine.telemedicine_backend.controller;

import com.telemedicine.telemedicine_backend.dto.ApiResponse;
import com.telemedicine.telemedicine_backend.dto.DoctorAvailabilityDTO;
import com.telemedicine.telemedicine_backend.dto.SlotAllocationRequestDTO;
import com.telemedicine.telemedicine_backend.entity.DoctorAvailability;
import com.telemedicine.telemedicine_backend.service.AvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    /**
     * Admin allocates EXACT slot times for a doctor on a specific date.
     * Request: { "doctorId": 1, "date": "2026-05-15", "slotTimes": ["09:00", "09:30", "10:00"] }
     */
    @PostMapping("/allocate")
    public ResponseEntity<ApiResponse<DoctorAvailabilityDTO>> allocateSlots(@RequestBody Map<String, Object> request) {
        Long doctorId = Long.parseLong(request.get("doctorId").toString());
        LocalDate date = LocalDate.parse(request.get("date").toString());
        
        @SuppressWarnings("unchecked")
        List<String> slotTimes = (List<String>) request.get("slotTimes");

        DoctorAvailability availability = availabilityService.allocateSlots(doctorId, date, slotTimes);
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO(
                availability.getId(),
                availability.getDoctor().getId(),
                availability.getDoctor().getName(),
                availability.getAvailableDate(),
                availability.getTotalSlots(),
                availability.getBookedCount(),
                availability.getSlotTimesList()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, dto));
    }

    /**
     * Admin allocates EXACT slot times for a doctor across a date range.
     * Same slot times apply to each day.
     * Request: { "doctorId": 1, "startDate": "2026-05-15", "endDate": "2026-05-23", "slotTimes": ["09:00", "10:00", "14:00"] }
     */
    @PostMapping("/allocate-range")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityDTO>>> allocateSlotsRange(@RequestBody Map<String, Object> request) {
        Long doctorId = Long.parseLong(request.get("doctorId").toString());
        LocalDate startDate = LocalDate.parse(request.get("startDate").toString());
        LocalDate endDate = LocalDate.parse(request.get("endDate").toString());
        
        @SuppressWarnings("unchecked")
        List<String> slotTimes = (List<String>) request.get("slotTimes");

        List<DoctorAvailability> availabilities = availabilityService.allocateSlotsForRange(doctorId, startDate, endDate, slotTimes);
        List<DoctorAvailabilityDTO> dtos = availabilities.stream()
                .map(a -> new DoctorAvailabilityDTO(
                        a.getId(),
                        a.getDoctor().getId(),
                        a.getDoctor().getName(),
                        a.getAvailableDate(),
                        a.getTotalSlots(),
                        a.getBookedCount(),
                        a.getSlotTimesList()
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, dtos));
    }

    /**
     * Get doctor's availability for a date range.
     * Query params: ?doctorId=1&startDate=2026-05-15&endDate=2026-05-23
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityDTO>>> getDoctorAvailability(
            @RequestParam Long doctorId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<DoctorAvailabilityDTO> availabilities = availabilityService.getDoctorAvailability(doctorId, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse<>(true, availabilities));
    }

    /**
     * Delete a specific doctor availability slot by ID.
     * Path param: /api/availability/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Slot deleted successfully"));
    }

}

