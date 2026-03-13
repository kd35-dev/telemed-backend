package com.telemedicine.telemedicine_backend.dto;

public class AiAnalysisResponseDTO {
    private String condition;
    private String severity;
    private String advice;

    public AiAnalysisResponseDTO(){}

    public AiAnalysisResponseDTO(String condition, String severity, String advice){
        this.condition = condition;
        this.severity = severity;
        this.advice = advice;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
