package com.telemedicine.telemedicine_backend.repository;

import com.telemedicine.telemedicine_backend.entity.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctors, Long> {
    List<Doctors> findBySpecialization(String specialization);
    List<Doctors> findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(String name, String specialization);
}
