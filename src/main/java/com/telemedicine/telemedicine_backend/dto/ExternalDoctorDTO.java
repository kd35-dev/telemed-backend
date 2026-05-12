package com.telemedicine.telemedicine_backend.dto;

public class ExternalDoctorDTO {
    private String source;
    private String name;
    private String specialization;
    private String address;
    private String contactNumber;
    private String website;
    private String placeId;
    private String providerType;
    private String visitMode;
    private Double distance; // distance in kilometers
    private Double latitude;
    private Double longitude;
    private Double rating;
    private Integer ratingCount;

    public ExternalDoctorDTO() {}

    public ExternalDoctorDTO(String source, String name, String specialization, String address, String contactNumber, String website, String placeId, String providerType, String visitMode) {
        this.source = source;
        this.name = name;
        this.specialization = specialization;
        this.address = address;
        this.contactNumber = contactNumber;
        this.website = website;
        this.placeId = placeId;
        this.providerType = providerType;
        this.visitMode = visitMode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getVisitMode() {
        return visitMode;
    }

    public void setVisitMode(String visitMode) {
        this.visitMode = visitMode;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }
}