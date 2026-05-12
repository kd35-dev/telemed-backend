package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.dto.AiAnalysisResponseDTO;
import com.telemedicine.telemedicine_backend.dto.ExternalDoctorDTO;
import com.telemedicine.telemedicine_backend.dto.SymptomRequestdto;
import com.telemedicine.telemedicine_backend.dto.SymptomResponsedto;
import com.telemedicine.telemedicine_backend.entity.Doctors;
import com.telemedicine.telemedicine_backend.enums.SeverityLevel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class SymptomService {

    private final AiService aiService;
    private final SeverityService severityService;
    private final DoctorService doctorService;
    private final SpecialistMappingService specialistMappingService;
    private final SymptomLogService symptomLogService;
    private final GoogleDoctorLookupService googleDoctorLookupService;
    private final AvailabilityService availabilityService;

    /** Same window as patient slot view: today through today + 6 (7 calendar days). */
    private static final int PATIENT_SLOT_WINDOW_DAYS = 7;

    public SymptomService(AiService aiService,
                          SeverityService severityService,
                          DoctorService doctorService,
                          SpecialistMappingService specialistMappingService,
                          SymptomLogService symptomLogService,
                          GoogleDoctorLookupService googleDoctorLookupService,
                          AvailabilityService availabilityService) {
        this.aiService = aiService;
        this.severityService = severityService;
        this.doctorService = doctorService;
        this.specialistMappingService = specialistMappingService;
        this.symptomLogService = symptomLogService;
        this.googleDoctorLookupService = googleDoctorLookupService;
        this.availabilityService = availabilityService;
    }

    public SymptomResponsedto analyzeSymptom(SymptomRequestdto request){
        String symptoms = request.getSymptoms();

        AiAnalysisResponseDTO aiResult = aiService.aiSymptom(symptoms);

        SeverityLevel sevirityLevel = severityService.evaluateSeverity(symptoms);

        String specialization = specialistMappingService.mapToSpecialization(aiResult.getSeverity());

        List<Doctors> recommendation = doctorService.getDoctorsBySpecialization(specialization);

        LocalDate windowStart = LocalDate.now();
        LocalDate windowEnd = windowStart.plusDays(PATIENT_SLOT_WINDOW_DAYS - 1);
        for (Doctors d : recommendation) {
            int liveOpen = availabilityService.sumAvailableSlotsInRange(d.getId(), windowStart, windowEnd);
            d.setAvailableSlots(liveOpen);
        }

        List<ExternalDoctorDTO> externalDoctors = googleDoctorLookupService.findDoctors(
                specialization,
                request.getLatitude(),
                request.getLongitude(),
                request.getLocationLabel());

        symptomLogService.saveLog(
                symptoms,
                aiResult.getCondition(),
                sevirityLevel.name(),
                specialization);

        String message = "Possible condition: " + aiResult.getCondition() +"."+ " Advice: " + aiResult.getAdvice()+".";
        SymptomResponsedto response = new SymptomResponsedto(message,sevirityLevel.name(),specialization,recommendation);
        response.setExternalDoctors(externalDoctors == null ? Collections.emptyList() : externalDoctors);
        return response;
    }
}