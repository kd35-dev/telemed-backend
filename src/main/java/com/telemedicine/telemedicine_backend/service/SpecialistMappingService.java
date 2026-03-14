package com.telemedicine.telemedicine_backend.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SpecialistMappingService {

    private final Map<String,String> keyword = Map.ofEntries(
            // General / Fever / Infection
            Map.entry("fever", "General Physician"),
            Map.entry("flu", "General Physician"),
            Map.entry("cold", "General Physician"),
            Map.entry("cough", "General Physician"),
            Map.entry("fatigue", "General Physician"),
            Map.entry("infection", "General Physician"),
            Map.entry("vomit", "General Physician"),
            Map.entry("diarrhea", "General Physician"),
            Map.entry("weakness", "General Physician"),
            Map.entry("headache", "General Physician"),

            // Dental
            Map.entry("tooth", "Dental Surgeon"),
            Map.entry("teeth", "Dental Surgeon"),
            Map.entry("gum", "Dental Surgeon"),
            Map.entry("cavity", "Dental Surgeon"),
            Map.entry("mouth sore", "Dental Surgeon"),
            Map.entry("jaw pain", "Dental Surgeon"),
            Map.entry("dental", "Dental Surgeon"),

            // Skin
            Map.entry("rash", "Skin Specialist"),
            Map.entry("acne", "Skin Specialist"),
            Map.entry("itch", "Skin Specialist"),
            Map.entry("skin", "Skin Specialist"),
            Map.entry("eczema", "Skin Specialist"),
            Map.entry("psoriasis", "Skin Specialist"),
            Map.entry("blisters", "Skin Specialist"),
            Map.entry("hives", "Skin Specialist"),

            // Gynaecology
            Map.entry("period", "Gynaecologist"),
            Map.entry("menstrual", "Gynaecologist"),
            Map.entry("pregnancy", "Gynaecologist"),
            Map.entry("ovarian", "Gynaecologist"),
            Map.entry("vaginal", "Gynaecologist"),
            Map.entry("uterus", "Gynaecologist"),
            Map.entry("pcos", "Gynaecologist")
    );

    private final String defaultSpecialization = "General Physician";

    public String mapToSpecialization(String conditon){
        if(conditon == null || conditon.isBlank()) return defaultSpecialization;

        String lower = conditon.toLowerCase();
        for(Map.Entry<String,String> entry : keyword.entrySet()){
            if(lower.contains(entry.getKey())) return entry.getValue();
        }
        return defaultSpecialization;
    }
}
