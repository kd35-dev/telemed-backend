package com.telemedicine.telemedicine_backend.dto;

public class AdminProfileDTO {
    private String username;

    public AdminProfileDTO() {}

    public AdminProfileDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
