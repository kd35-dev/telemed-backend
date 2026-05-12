package com.telemedicine.telemedicine_backend.dto;

public class LoginResponseDTO {

    private String token;
    private String username;
    private String role;
    private String displayName;
    private String message;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, String username, String role, String displayName, String message) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.displayName = displayName;
        this.message = message;
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getDisplayName() { return displayName; }
    public String getMessage() { return message; }

    public void setToken(String token) { this.token = token; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(String role) { this.role = role; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setMessage(String message) { this.message = message; }
}