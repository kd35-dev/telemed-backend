package com.telemedicine.telemedicine_backend.controller;

import com.telemedicine.telemedicine_backend.dto.ApiResponse;
import com.telemedicine.telemedicine_backend.dto.AppointmentBookingRequestDTO;
import com.telemedicine.telemedicine_backend.dto.AppointmentDTO;
import com.telemedicine.telemedicine_backend.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<AppointmentDTO>> bookAppointment(@RequestBody AppointmentBookingRequestDTO request) {
        AppointmentDTO appointment = appointmentService.bookAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, appointment));
    }

    @GetMapping("/occupied")
    public ResponseEntity<ApiResponse<List<String>>> getOccupiedTimes(@RequestParam Long doctorId,
                                                                      @RequestParam LocalDate date) {
        List<String> occupiedTimes = appointmentService.getOccupiedTimes(doctorId, date);
        return ResponseEntity.ok(new ApiResponse<>(true, occupiedTimes));
    }
}
