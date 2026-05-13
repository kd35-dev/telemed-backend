package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.config.AiConfig;
import com.telemedicine.telemedicine_backend.dto.AiAnalysisResponseDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private static final Logger logger = LoggerFactory.getLogger(AiService.class);

    private final AiConfig aiConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AiService(AiConfig aiConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.aiConfig = aiConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public AiAnalysisResponseDTO aiSymptom(String symptom) {


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiConfig.getAiApiKey());

        Map<String, Object> message = Map.of("role", "user",
                "content",
                """
You are a medical symptom analysis assistant.

Analyze the symptoms and respond ONLY in JSON format.

{
 "condition": "...",
 "severity": "...",
 "advice": "..."
}

Rules:
- condition should be the most likely illness
- severity must be from the given list["fever","flu","cold","cough","fatigue","infection","vomit",
"diarrhea","weakness","headache","tooth","teeth","gum","cavity","mouth sore","jaw pain","dental",
"rash","acne","itch","skin","eczema","psoriasis","blisters","hives","period","menstrual","pregnancy"
"ovarian","vaginal","uterus","pcos"]
- advice should be short (1–2 sentences)

Symptoms: """ + symptom);
        Map<String, Object> requestBody = Map.of("model", "llama-3.3-70b-versatile", "messages", List.of(message));


        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            String response = restTemplate.postForObject(aiConfig.getAiApiUrl(), httpEntity, String.class);
            JsonNode root = objectMapper.readTree(response);

            String extractedText = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            JsonNode extractedTextroot = objectMapper.readTree(extractedText);
            String condition = extractedTextroot.path("condition").asText();
            String advice = extractedTextroot.path("advice").asText();
            String severity = extractedTextroot.path("severity").asText();

            return new AiAnalysisResponseDTO(
                    condition,
                    severity,
                    advice
            );
        } catch (RestClientException e) {
            logger.warn("Groq API call failed: {}", e.getMessage());
        }
        return new AiAnalysisResponseDTO("Unknown Condition",
                "Unknown Severity",
                "Unable to analyze symptoms at the moment. Please consult a doctor.");
    }
}
