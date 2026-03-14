package com.telemedicine.telemedicine_backend.controller;

import com.telemedicine.telemedicine_backend.dto.ApiResponse;
import com.telemedicine.telemedicine_backend.entity.SymptomLog;
import com.telemedicine.telemedicine_backend.service.SymptomLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class SymptomLogController {

    private final SymptomLogService symptomLogService;

    public SymptomLogController(SymptomLogService symptomLogService) {
        this.symptomLogService = symptomLogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SymptomLog>>> getAllLogs() {
        List<SymptomLog> logs = symptomLogService.getAllLogs();
        ApiResponse<List<SymptomLog>> response = new ApiResponse<>(true, logs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<ApiResponse<List<SymptomLog>>> getLogsBySeverity(
            @PathVariable String severity) {
        List<SymptomLog> logs = symptomLogService.getLogsBySeverity(severity);
        ApiResponse<List<SymptomLog>> response = new ApiResponse<>(true, logs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<ApiResponse<List<SymptomLog>>> getLogsBySpecialization(
            @PathVariable String specialization) {
        List<SymptomLog> logs = symptomLogService.getLogsBySpecialization(specialization);
        ApiResponse<List<SymptomLog>> response = new ApiResponse<>(true, logs);
        return ResponseEntity.ok(response);
    }
}