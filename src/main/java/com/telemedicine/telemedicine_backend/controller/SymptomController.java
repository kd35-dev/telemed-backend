package com.telemedicine.telemedicine_backend.controller;

import com.telemedicine.telemedicine_backend.dto.ApiResponse;
import com.telemedicine.telemedicine_backend.dto.SymptomRequestdto;
import com.telemedicine.telemedicine_backend.dto.SymptomResponsedto;
import com.telemedicine.telemedicine_backend.service.SymptomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/symptoms")
public class SymptomController {

    private final SymptomService symptomService;

    public SymptomController(SymptomService symptomService){
        this.symptomService = symptomService;
    }

    @PostMapping("/analysis")
    public ResponseEntity<ApiResponse<SymptomResponsedto>> analysisSymptom(@Valid @RequestBody SymptomRequestdto request){
        SymptomResponsedto symptomresponse = symptomService.analyzeSymptom(request);
        ApiResponse<SymptomResponsedto> response = new ApiResponse<>(true,symptomresponse);
        return ResponseEntity.ok(response);
    }

}
