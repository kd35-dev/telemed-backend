package com.telemedicine.telemedicine_backend.repository;

import com.telemedicine.telemedicine_backend.entity.SymptomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymptomLogRepository extends JpaRepository<SymptomLog, Long> {
    List<SymptomLog> findBySeverity(String severity);
    List<SymptomLog> findBySpecialization(String specialization);
}