package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.dto.AiAnalysisResponseDTO;
import com.telemedicine.telemedicine_backend.dto.SymptomResponsedto;
import com.telemedicine.telemedicine_backend.entity.Doctors;
import com.telemedicine.telemedicine_backend.enums.SeverityLevel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SymptomService {

    private final AiService aiService;
    private final SeverityService severityService;
    private final DoctorService doctorService;
    private final SpecialistMappingService specialistMappingService;

    public SymptomService(AiService aiService, SeverityService severityService,DoctorService doctorService, SpecialistMappingService specialistMappingService){
        this.aiService = aiService;
        this.severityService = severityService;
        this.doctorService = doctorService;
        this.specialistMappingService = specialistMappingService;
    }

    public SymptomResponsedto analyzeSymptom(String symptoms){
        AiAnalysisResponseDTO aiResult = aiService.aiSymptom(symptoms);
        SeverityLevel sevirityLevel = severityService.evaluateSeverity(symptoms);
        String specialization = specialistMappingService.mapToSpecialization(aiResult.getCondition());
        List<Doctors> recommendation = doctorService.getDoctorsBySpecialization(specialization);
        String message = "Possible condition: " + aiResult.getCondition() +"."+ " Advice: " + aiResult.getAdvice()+".";
        return new SymptomResponsedto(message,sevirityLevel.name(),specialization,recommendation);
    }
}
