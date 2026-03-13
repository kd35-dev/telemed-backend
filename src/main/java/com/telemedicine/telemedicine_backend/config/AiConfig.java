package com.telemedicine.telemedicine_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AiConfig {

    @Value("${ai.api.key}")
    private String aiApiKey;

    @Value("${ai.api.url}")
    private String aiApiUrl;

    public String getAiApiKey() {
        return aiApiKey;
    }

    public String getAiApiUrl() {
        return aiApiUrl;
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
