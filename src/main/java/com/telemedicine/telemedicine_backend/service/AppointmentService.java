package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.dto.AppointmentBookingRequestDTO;
import com.telemedicine.telemedicine_backend.dto.AppointmentDTO;
import com.telemedicine.telemedicine_backend.entity.Appointment;
import com.telemedicine.telemedicine_backend.entity.DoctorAvailability;
import com.telemedicine.telemedicine_backend.entity.Doctors;
import com.telemedicine.telemedicine_backend.repository.AppointmentRepository;
import com.telemedicine.telemedicine_backend.repository.DoctorAvailabilityRepository;
import com.telemedicine.telemedicine_backend.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              DoctorAvailabilityRepository availabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Transactional
    public AppointmentDTO bookAppointment(AppointmentBookingRequestDTO request) {
        validateBookingRequest(request);

        Doctors doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        DoctorAvailability availability = availabilityRepository
                .findByDoctorIdAndAvailableDate(request.getDoctorId(), request.getAppointmentDate())
                .orElseThrow(() -> new IllegalArgumentException("Doctor has no allocated slots on the selected date"));

        if (availability.getAvailableSlots() <= 0) {
            throw new IllegalArgumentException("No slots available for the selected date");
        }

        if (appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new IllegalArgumentException("Selected time slot is already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setPatientName(request.getPatientName().trim());
        appointment.setPatientPhone(request.getPatientPhone().trim());
        appointment.setPatientEmail(trimToNull(request.getPatientEmail()));
        appointment.setSymptoms(trimToNull(request.getSymptoms()));
        appointment.setNotes(trimToNull(request.getNotes()));

        Appointment saved = appointmentRepository.save(appointment);

        availability.setBookedCount(availability.getBookedCount() + 1);
        availabilityRepository.save(availability);

        return toDto(saved, availability.getAvailableSlots());
    }

    public List<String> getOccupiedTimes(Long doctorId, LocalDate appointmentDate) {
        return appointmentRepository
                .findByDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(doctorId, appointmentDate)
                .stream()
                .map(a -> a.getAppointmentTime().toString())
                .toList();
    }

    private void validateBookingRequest(AppointmentBookingRequestDTO request) {
        if (request.getDoctorId() == null) {
            throw new IllegalArgumentException("Doctor is required");
        }
        if (request.getAppointmentDate() == null) {
            throw new IllegalArgumentException("Appointment date is required");
        }
        if (request.getAppointmentTime() == null) {
            throw new IllegalArgumentException("Appointment time is required");
        }
        if (request.getPatientName() == null || request.getPatientName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required");
        }
        if (request.getPatientPhone() == null || request.getPatientPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient phone is required");
        }

        LocalDateTime appointmentDateTime = LocalDateTime.of(request.getAppointmentDate(), request.getAppointmentTime());
        if (!appointmentDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Please select a future date and time");
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private AppointmentDTO toDto(Appointment appointment, int remainingSlots) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setDoctorName(appointment.getDoctor().getName());
        dto.setSpecialization(appointment.getDoctor().getSpecialization());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setPatientName(appointment.getPatientName());
        dto.setPatientPhone(appointment.getPatientPhone());
        dto.setPatientEmail(appointment.getPatientEmail());
        dto.setSymptoms(appointment.getSymptoms());
        dto.setNotes(appointment.getNotes());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setRemainingSlots(remainingSlots);
        return dto;
    }
}
