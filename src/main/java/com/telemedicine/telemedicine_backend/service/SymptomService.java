package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.dto.AiAnalysisResponseDTO;
import com.telemedicine.telemedicine_backend.dto.SymptomResponsedto;
import org.springframework.stereotype.Service;

@Service
public class SymptomService {

    private final AiService aiService;

    public SymptomService(AiService aiService){
        this.aiService = aiService;
    }

    public SymptomResponsedto analyzeSymptom(String symptoms){
        AiAnalysisResponseDTO aiResult = aiService.aiSymptom(symptoms);
        String message = "Possible condition: " + aiResult.getCondition() +"."+ " Advice: " + aiResult.getAdvice()+".";
        return new SymptomResponsedto(message,aiResult.getSeverity());
    }
}
