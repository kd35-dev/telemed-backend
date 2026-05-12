package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.dto.ExternalDoctorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.List;
import java.util.Set;

@Service
public class GoogleDoctorLookupService {

    private static final Logger log = LoggerFactory.getLogger(GoogleDoctorLookupService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${google.places.api.key:}")
    private String apiKey;

    @Value("${google.places.text-search-url:https://maps.googleapis.com/maps/api/place/textsearch/json}")
    private String textSearchUrl;

    @Value("${google.places.details-url:https://maps.googleapis.com/maps/api/place/details/json}")
    private String detailsUrl;

    @Value("${google.places.nearby-search-url:https://maps.googleapis.com/maps/api/place/nearbysearch/json}")
    private String nearbySearchUrl;

    @Value("${google.places.search-radius-meters:5000}")
    private int searchRadiusMeters;

    public GoogleDoctorLookupService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<ExternalDoctorDTO> findDoctors(String specialization, Double latitude, Double longitude, String locationLabel) {
        if (apiKey == null || apiKey.isBlank() || specialization == null || specialization.isBlank()) {
            return List.of();
        }

        List<SearchRequest> searches = buildSearches(specialization, latitude, longitude);
        List<ExternalDoctorDTO> doctors = new ArrayList<>();
        Set<String> seenPlaceIds = new LinkedHashSet<>();

        try {
            for (SearchRequest searchRequest : searches) {
                if (doctors.size() >= 8) {
                    break;
                }

                String searchUrl = buildSearchUrl(searchRequest, specialization, latitude, longitude);
                String response = restTemplate.getForObject(searchUrl, String.class);
                if (response == null || response.isBlank()) {
                    continue;
                }

                JsonNode root = objectMapper.readTree(response);
                JsonNode results = root.path("results");

                if (!results.isArray()) {
                    continue;
                }

                for (JsonNode item : results) {
                    if (doctors.size() >= 8) {
                        break;
                    }

                    String placeId = item.path("place_id").asText("");
                    if (!placeId.isBlank() && !seenPlaceIds.add(placeId)) {
                        continue;
                    }

                    String name = item.path("name").asText("");
                    String address = item.path("vicinity").asText(item.path("formatted_address").asText(""));
                    String phoneNumber = "";
                    String website = "";
                    String providerType = searchRequest.providerType;
                    Double placeLatitude = null;
                    Double placeLongitude = null;
                    Double distance = null;
                    Double rating = null;
                    Integer ratingCount = null;

                    // Extract geometry if available (for nearby search)
                    JsonNode geometryNode = item.path("geometry");
                    if (!geometryNode.isMissingNode()) {
                        JsonNode locNode = geometryNode.path("location");
                        if (!locNode.isMissingNode()) {
                            placeLatitude = locNode.path("lat").asDouble();
                            placeLongitude = locNode.path("lng").asDouble();
                            
                            // Calculate distance if user location is available
                            if (latitude != null && longitude != null && placeLatitude != null && placeLongitude != null) {
                                distance = calculateDistance(latitude, longitude, placeLatitude, placeLongitude);
                            }
                        }
                    }

                    // Extract rating if available
                    if (item.has("rating")) {
                        rating = item.path("rating").asDouble();
                    }
                    if (item.has("user_ratings_total")) {
                        ratingCount = item.path("user_ratings_total").asInt();
                    }

                    if (!placeId.isBlank()) {
                        String detailUrl = UriComponentsBuilder.fromUriString(detailsUrl)
                                .queryParam("place_id", placeId)
                                .queryParam("fields", "formatted_phone_number,website,formatted_address,name,types,rating,user_ratings_total,geometry")
                                .queryParam("key", apiKey)
                                .toUriString();
                        try {
                            String detailResponse = restTemplate.getForObject(detailUrl, String.class);
                            if (detailResponse != null && !detailResponse.isBlank()) {
                                JsonNode detailRoot = objectMapper.readTree(detailResponse);
                                JsonNode detailResult = detailRoot.path("result");
                                phoneNumber = detailResult.path("formatted_phone_number").asText("");
                                website = detailResult.path("website").asText("");
                                String detailedAddress = detailResult.path("formatted_address").asText("");
                                if (!detailedAddress.isBlank()) {
                                    address = detailedAddress;
                                }
                                providerType = inferProviderType(detailResult.path("types"), providerType);
                                
                                // Extract detailed rating info
                                if (detailResult.has("rating")) {
                                    rating = detailResult.path("rating").asDouble();
                                }
                                if (detailResult.has("user_ratings_total")) {
                                    ratingCount = detailResult.path("user_ratings_total").asInt();
                                }
                                
                                // Extract geometry from detail if not already available
                                if (placeLatitude == null || placeLongitude == null) {
                                    JsonNode detailGeometry = detailResult.path("geometry");
                                    if (!detailGeometry.isMissingNode()) {
                                        JsonNode detailLocNode = detailGeometry.path("location");
                                        if (!detailLocNode.isMissingNode()) {
                                            placeLatitude = detailLocNode.path("lat").asDouble();
                                            placeLongitude = detailLocNode.path("lng").asDouble();
                                            if (latitude != null && longitude != null) {
                                                distance = calculateDistance(latitude, longitude, placeLatitude, placeLongitude);
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception detailError) {
                            log.warn("Google provider detail lookup failed for placeId {}: {}", placeId, detailError.getMessage());
                        }
                    }

                    ExternalDoctorDTO doctor = new ExternalDoctorDTO(
                            "Google Nearby",
                            name,
                            specialization,
                            address,
                            phoneNumber,
                            website,
                            placeId,
                            providerType,
                            "Physical visit"
                    );
                    doctor.setDistance(distance);
                    doctor.setLatitude(placeLatitude);
                    doctor.setLongitude(placeLongitude);
                    doctor.setRating(rating);
                    doctor.setRatingCount(ratingCount);
                    
                    doctors.add(doctor);
                }
            }

            return doctors;
        } catch (Exception e) {
            log.warn("Google nearby provider lookup failed for {}: {}", specialization, e.getMessage());
            return List.of();
        }
    }

    private List<SearchRequest> buildSearches(String specialization, Double latitude, Double longitude) {
        List<SearchRequest> searches = new ArrayList<>();
        searches.add(new SearchRequest(specialization + " doctor", "Doctor"));
        searches.add(new SearchRequest(specialization + " clinic", "Clinic"));
        searches.add(new SearchRequest(specialization + " hospital", "Hospital"));
        searches.add(new SearchRequest(specialization + " offline consultation", "Consultation Center"));
        searches.add(new SearchRequest(specialization + " physical visit", "Physical Visit Clinic"));
        return searches;
    }

    private String buildSearchUrl(SearchRequest searchRequest, String specialization, Double latitude, Double longitude) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(latitude != null && longitude != null ? nearbySearchUrl : textSearchUrl)
                .queryParam("key", apiKey);

        if (latitude != null && longitude != null) {
            builder.queryParam("location", latitude + "," + longitude)
                    .queryParam("radius", searchRadiusMeters)
                    .queryParam("keyword", searchRequest.query)
                    .queryParam("type", searchRequest.type);
        } else {
            builder.queryParam("query", searchRequest.query + " near me")
                    .queryParam("region", "in");
        }

        return builder.toUriString();
    }

    private String inferProviderType(JsonNode typesNode, String fallback) {
        if (typesNode != null && typesNode.isArray()) {
            for (JsonNode typeNode : typesNode) {
                String type = typeNode.asText("").toLowerCase(Locale.ROOT);
                if (type.contains("hospital")) {
                    return "Hospital";
                }
                if (type.contains("clinic") || type.contains("doctor") || type.contains("medical") || type.contains("health")) {
                    return "Clinic";
                }
                if (type.contains("pharmacy") || type.contains("healthcare") || type.contains("physician")) {
                    return "Consultation Center";
                }
            }
        }
        return fallback == null || fallback.isBlank() ? "Clinic" : fallback;
    }

    /**
     * Calculate distance between two coordinates using Haversine formula (in kilometers)
     */
    private Double calculateDistance(Double userLat, Double userLng, Double placeLat, Double placeLng) {
        final int R = 6371; // Earth's radius in kilometers
        double latDistance = Math.toRadians(placeLat - userLat);
        double lngDistance = Math.toRadians(placeLng - userLng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(placeLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // distance in kilometers
    }

    private static class SearchRequest {
        private final String query;
        private final String type;
        private final String providerType;

        private SearchRequest(String query, String providerType) {
            this.query = query;
            this.providerType = providerType;
            this.type = "doctor";
        }
    }
}