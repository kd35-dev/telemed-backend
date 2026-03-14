package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.entity.SymptomLog;
import com.telemedicine.telemedicine_backend.repository.SymptomLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SymptomLogService {
    private final SymptomLogRepository symptomLogRepository;

    public SymptomLogService(SymptomLogRepository symptomLogRepository) {
        this.symptomLogRepository = symptomLogRepository;
    }

    public void saveLog(String symptom, String condition, String severity, String specialization){

        SymptomLog symptomLog = new SymptomLog(
                symptom,
                condition,
                severity,
                specialization,
                LocalDateTime.now()
        );
        symptomLogRepository.save(symptomLog);

    }
    public List<SymptomLog> getAllLogs() {
        return symptomLogRepository.findAll();
    }

    public List<SymptomLog> getLogsBySeverity(String severity) {
        return symptomLogRepository.findBySeverity(severity);
    }

    public List<SymptomLog> getLogsBySpecialization(String specialization) {
        return symptomLogRepository.findBySpecialization(specialization);
    }
}
