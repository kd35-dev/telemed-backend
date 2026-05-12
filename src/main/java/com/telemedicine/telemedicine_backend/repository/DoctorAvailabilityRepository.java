package com.telemedicine.telemedicine_backend.repository;

import com.telemedicine.telemedicine_backend.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
    Optional<DoctorAvailability> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate date);
    List<DoctorAvailability> findByDoctorIdAndAvailableDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate);
    List<DoctorAvailability> findByAvailableDateBetween(LocalDate startDate, LocalDate endDate);
    List<DoctorAvailability> findByDoctorId(Long doctorId);
}
