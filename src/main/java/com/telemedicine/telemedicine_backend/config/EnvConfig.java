package com.telemedicine.telemedicine_backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to load environment variables from .env file
 * This ensures that all ${VARIABLE_NAME} placeholders in application.properties
 * are resolved from the .env file (for local development)
 */
@Configuration
public class EnvConfig {

    /**
     * Static initializer block runs before Spring context initialization
     * Loads .env file from project root and sets system properties
     */
    static {
        // Load .env file from project root
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()  // Don't fail if .env doesn't exist
                .load();

        // Set all env variables as system properties for Spring to resolve
        dotenv.entries().forEach(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();

            // Prefer process environment (Railway, shell) over .env.
            if (System.getenv(key) != null) {
                return;
            }
            if (System.getProperty(key) == null) {
                System.setProperty(key, value);
            }
        });

    }
}
