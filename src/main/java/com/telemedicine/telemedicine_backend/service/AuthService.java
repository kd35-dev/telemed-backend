package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.dto.AdminProfileDTO;
import com.telemedicine.telemedicine_backend.dto.ChangePasswordRequestDTO;
import com.telemedicine.telemedicine_backend.dto.LoginRequestDTO;
import com.telemedicine.telemedicine_backend.dto.LoginResponseDTO;
import com.telemedicine.telemedicine_backend.entity.Admin;
import com.telemedicine.telemedicine_backend.repository.AdminRepository;
import com.telemedicine.telemedicine_backend.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AdminRepository adminRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        String role = normalizeRole(request.getRole());

        Admin admin = adminRepository.findByUsernameAndRole(request.getUsername(), role)
                .orElseThrow(() -> new RuntimeException("Invalid username, password, or role"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid username, password, or role");
        }

        String adminRole = admin.getRole() != null ? admin.getRole() : "ADMIN";
        String token = jwtUtil.generateToken(admin.getUsername(), adminRole);

        return new LoginResponseDTO(token, admin.getUsername(), adminRole, admin.getDisplayName(), "Login successful");
    }

    public AdminProfileDTO getProfile(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        return new AdminProfileDTO(admin.getUsername());
    }

    public void changePassword(String username, ChangePasswordRequestDTO request) {
        if (request.getNewPassword() == null || request.getNewPassword().length() < 8) {
            throw new RuntimeException("New password must be at least 8 characters long");
        }

        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), admin.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        adminRepository.save(admin);
    }

    public void registerAdmin(String username, String rawPassword) {
        registerAccount(username, rawPassword, "ADMIN", username);
    }

    public void registerAdmin(String username, String rawPassword, String displayName) {
        registerAccount(username, rawPassword, "ADMIN", displayName);
    }

    private void registerAccount(String username, String rawPassword, String role, String displayName) {
        if (adminRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Account already exists with username: " + username);
        }
        Admin account = new Admin(username, passwordEncoder.encode(rawPassword), normalizeRole(role), displayName);
        adminRepository.save(account);
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "ADMIN";
        }
        return role.trim().toUpperCase();
    }
}
