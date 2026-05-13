package com.telemedicine.telemedicine_backend.config;

import com.telemedicine.telemedicine_backend.repository.AdminRepository;
import com.telemedicine.telemedicine_backend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Value("${app.default-admin.username:}")
    private String defaultAdminUsername;

    @Value("${app.default-admin.password:}")
    private String defaultAdminPassword;

    @Value("${app.default-admin.display-name:Administrator}")
    private String defaultAdminDisplayName;

    @Bean
    public CommandLineRunner createDefaultAdmin(AuthService authService, AdminRepository adminRepository) {
        return args -> {
            if (!isBlank(defaultAdminUsername) && !isBlank(defaultAdminPassword)) {
                try {
                    authService.registerAdmin(defaultAdminUsername, defaultAdminPassword, defaultAdminDisplayName);
                    logger.info("Default admin account created.");
                } catch (RuntimeException e) {
                    if (adminRepository.findByUsername(defaultAdminUsername).isPresent()) {
                        logger.info("Default admin account already exists.");
                    } else {
                        throw e;
                    }
                }
            }
        };
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
