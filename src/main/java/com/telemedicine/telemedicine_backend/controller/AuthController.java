package com.telemedicine.telemedicine_backend.controller;

import com.telemedicine.telemedicine_backend.dto.AdminProfileDTO;
import com.telemedicine.telemedicine_backend.dto.ApiResponse;
import com.telemedicine.telemedicine_backend.dto.ChangePasswordRequestDTO;
import com.telemedicine.telemedicine_backend.dto.LoginRequestDTO;
import com.telemedicine.telemedicine_backend.dto.LoginResponseDTO;
import com.telemedicine.telemedicine_backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @RequestBody LoginRequestDTO request) {
        LoginResponseDTO loginResponse = authService.login(request);
        ApiResponse<LoginResponseDTO> response = new ApiResponse<>(true, loginResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AdminProfileDTO>> getProfile(Authentication authentication) {
        AdminProfileDTO profile = authService.getProfile(authentication.getName());
        ApiResponse<AdminProfileDTO> response = new ApiResponse<>(true, profile);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequestDTO request) {
        authService.changePassword(authentication.getName(), request);
        ApiResponse<String> response = new ApiResponse<>(true, "Password changed successfully");
        return ResponseEntity.ok(response);
    }
}