package com.telemedicine.telemedicine_backend.dto;

public class LoginRequestDTO {

    private String username;
    private String password;
    private String role;

    public LoginRequestDTO() {}

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}