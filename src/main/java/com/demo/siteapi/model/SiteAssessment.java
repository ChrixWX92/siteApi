package com.demo.siteapi.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a prospect site assessment used in subsurface evaluation.
 * <p>
 * Each assessment records a geographical location, estimated volume,
 * a confidence level and a workflow status. The entity is persisted
 * in an H2 relational database via JPA.
 */
@Entity
@Table(name = "site_assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteAssessment {

    /**
     * Unique identifier, automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Human‑readable name for the site (maximum 100 characters).
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Latitude in decimal degrees. Must be between -90 and 90.
     */
    private double latitude;

    /**
     * Longitude in decimal degrees. Must be between -180 and 180.
     */
    private double longitude;

    /**
     * Geological basin or region (e.g. "Permian", "Gulf of Mexico").
     */
    @Column(nullable = false)
    private String basin;

    /**
     * Estimated extractable volume (in arbitrary units). May be null.
     */
    private BigDecimal estimatedVolume;

    /**
     * Confidence level assigned to the assessment.
     */
    @Enumerated(EnumType.STRING)
    private Confidence confidence;

    /**
     * Current stage in the review workflow.
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * Timestamp of when the assessment was created. Set automatically.
     */
    private Instant createdAt;

    /**
     * Convenience constructor that sets {@link #createdAt} to now.
     */
    public SiteAssessment(String name, double latitude, double longitude,
                          String basin, BigDecimal estimatedVolume,
                          Confidence confidence, Status status) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.basin = basin;
        this.estimatedVolume = estimatedVolume;
        this.confidence = confidence;
        this.status = status;
        this.createdAt = Instant.now();
    }
}