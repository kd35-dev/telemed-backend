package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.dto.DoctorStatsDTO;
import com.telemedicine.telemedicine_backend.entity.Doctors;
import com.telemedicine.telemedicine_backend.repository.DoctorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    public final DoctorRepository doctorRepository;
    private final AuditLogService auditLogService;

    public DoctorService(DoctorRepository doctorRepository, AuditLogService auditLogService){
        this.doctorRepository = doctorRepository;
        this.auditLogService = auditLogService;
    }

    private String getCurrentAdminUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "SYSTEM";
    }

    public Doctors saveDoctors(Doctors doctors){
        Doctors saved = doctorRepository.save(doctors);
        auditLogService.logAction(getCurrentAdminUsername(), "ADD_DOCTOR", "DOCTOR", saved.getId(),
                "Doctor added: " + doctors.getName() + " (" + doctors.getSpecialization() + ")");
        return saved;
    }

    public List<Doctors> getAllDoctors(){
        return doctorRepository.findAll();
    }

    public Doctors getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + id));
    }

    public List<Doctors> searchDoctors(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllDoctors();
        }
        return doctorRepository.findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(query, query);
    }

    public Doctors updateDoctor(Long id, Doctors update) {
        Doctors doctor = getDoctorById(id);
        String oldDetails = doctor.getName() + " (" + doctor.getSpecialization() + ", " + doctor.getAvailableSlots() + " slots)";
        
        doctor.setName(update.getName());
        doctor.setSpecialization(update.getSpecialization());
        doctor.setAvailableSlots(update.getAvailableSlots());
        
        Doctors updated = doctorRepository.save(doctor);
        
        String newDetails = updated.getName() + " (" + updated.getSpecialization() + ", " + updated.getAvailableSlots() + " slots)";
        auditLogService.logAction(getCurrentAdminUsername(), "EDIT_DOCTOR", "DOCTOR", id,
                "Doctor updated: " + oldDetails + " → " + newDetails);
        
        return updated;
    }

    public void deleteDoctor(Long id) {
        Doctors doctor = getDoctorById(id);
        doctorRepository.deleteById(id);
        auditLogService.logAction(getCurrentAdminUsername(), "DELETE_DOCTOR", "DOCTOR", id,
                "Doctor deleted: " + doctor.getName() + " (" + doctor.getSpecialization() + ")");
    }

    public DoctorStatsDTO getDoctorStatistics() {
        List<Doctors> doctors = getAllDoctors();
        Map<String, Long> counts = doctors.stream()
                .collect(Collectors.groupingBy(Doctors::getSpecialization, Collectors.counting()));

        int totalAvailableSlots = doctors.stream().mapToInt(Doctors::getAvailableSlots).sum();

        return new DoctorStatsDTO(
                doctors.size(),
                counts.size(),
                totalAvailableSlots,
                counts
        );
    }

    public List<Doctors> getDoctorsBySpecialization(String specialization){
        return doctorRepository.findBySpecialization(specialization);
    }
}
