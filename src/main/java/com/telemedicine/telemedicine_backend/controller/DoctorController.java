package com.telemedicine.telemedicine_backend.controller;

import com.telemedicine.telemedicine_backend.dto.ApiResponse;
import com.telemedicine.telemedicine_backend.dto.DoctorStatsDTO;
import com.telemedicine.telemedicine_backend.entity.Doctors;
import com.telemedicine.telemedicine_backend.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Doctors>> addDoctor(@RequestBody Doctors doctors){
        Doctors savedoctor = doctorService.saveDoctors(doctors);
        ApiResponse<Doctors> response = new ApiResponse<>(true,savedoctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Doctors>>> getAllDoctors(){
        List<Doctors> doctors = doctorService.getAllDoctors();
        ApiResponse<List<Doctors>> response = new ApiResponse<>(true, doctors);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Doctors>>> searchDoctors(@RequestParam(required = false) String query){
        List<Doctors> doctors = doctorService.searchDoctors(query);
        ApiResponse<List<Doctors>> response = new ApiResponse<>(true, doctors);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DoctorStatsDTO>> getDoctorStats(){
        DoctorStatsDTO stats = doctorService.getDoctorStatistics();
        ApiResponse<DoctorStatsDTO> response = new ApiResponse<>(true, stats);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Doctors>> updateDoctor(@PathVariable Long id,
                                                             @RequestBody Doctors doctors){
        Doctors updatedDoctor = doctorService.updateDoctor(id, doctors);
        ApiResponse<Doctors> response = new ApiResponse<>(true, updatedDoctor);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDoctor(@PathVariable Long id){
        doctorService.deleteDoctor(id);
        ApiResponse<String> response = new ApiResponse<>(true, "Doctor deleted successfully");
        return ResponseEntity.ok(response);
    }

}

