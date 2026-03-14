package com.telemedicine.telemedicine_backend.dto;

import com.telemedicine.telemedicine_backend.entity.Doctors;

import java.util.List;

public class SymptomResponsedto {
    private String message;
    private String severity;
    private String recommendSpecialization;
    private List<Doctors> recommenddoctors;

    public SymptomResponsedto(){}

    public SymptomResponsedto(String message, String severity, String recommendSpecialization,List<Doctors> recommenddoctors){
        this.message = message;
        this.severity = severity;
        this.recommendSpecialization = recommendSpecialization;
        this.recommenddoctors = recommenddoctors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getRecommendSpecialization() {
        return recommendSpecialization;
    }

    public void setRecommendSpecialization(String recommendSpecialization) {
        this.recommendSpecialization = recommendSpecialization;
    }

    public List<Doctors> getRecommenddoctors() {
        return recommenddoctors;
    }

    public void setRecommenddoctors(List<Doctors> recommenddoctors) {
        this.recommenddoctors = recommenddoctors;
    }
}