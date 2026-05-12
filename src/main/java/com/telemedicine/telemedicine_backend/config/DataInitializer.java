package com.telemedicine.telemedicine_backend.config;

import com.telemedicine.telemedicine_backend.service.AuthService;
import com.telemedicine.telemedicine_backend.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner createDefaultAdmin(AuthService authService, AdminRepository adminRepository) {
        return args -> {
            // Admin account
            try {
                authService.registerAdmin("admin", "admin123", "Administrator");
                System.out.println("✓ Default admin created — username: admin, password: admin123");
            } catch (RuntimeException e) {
                try {
                    // Try to update existing admin account
                    var admin = adminRepository.findByUsername("admin");
                    if (admin.isPresent()) {
                        System.out.println("✓ Admin account already exists");
                    } else {
                        System.out.println("✗ Error creating admin: " + e.getMessage());
                    }
                } catch (Exception ex) {
                    System.out.println("✗ Error: " + ex.getMessage());
                }
            }

            // Doctor account
            try {
                authService.registerDoctor("doctor", "doctor123", "Dr. Demo Doctor");
                System.out.println("✓ Default doctor account created — username: doctor, password: doctor123");
            } catch (RuntimeException e) {
                System.out.println("✓ Doctor account already exists, skipping creation.");
            }

            // Patient account
            try {
                authService.registerPatient("patient", "patient123", "Demo Patient");
                System.out.println("✓ Default patient account created — username: patient, password: patient123");
            } catch (RuntimeException e) {
                System.out.println("✓ Patient account already exists, skipping creation.");
            }
        };
    }
}