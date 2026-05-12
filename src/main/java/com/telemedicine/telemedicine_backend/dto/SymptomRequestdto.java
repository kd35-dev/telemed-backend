package com.telemedicine.telemedicine_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class SymptomRequestdto {

    @NotBlank(message = "Symptoms cannot not be blank")
    private String symptoms;
    private Double latitude;
    private Double longitude;
    private String locationLabel;

    public SymptomRequestdto(){}

    public SymptomRequestdto(String symptoms){
        this.symptoms = symptoms;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }
}
