package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.entity.Doctors;
import com.telemedicine.telemedicine_backend.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    public final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }

    public Doctors saveDoctors(Doctors doctors){
        return doctorRepository.save(doctors);
    }

    public List<Doctors> getAllDoctors(){
        return doctorRepository.findAll();
    }

    public List<Doctors> getDoctorsBySpecialization(String specialization){
        return doctorRepository.findBySpecialization(specialization);
    }

//    public List<Doctors> recommendDoctors(String specialization){
//        return doctorRepository.findBySpecialization(specialization);
//    }
}
