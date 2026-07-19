package com.demo.siteapi.dto;

import com.demo.siteapi.model.Confidence;
import com.demo.siteapi.model.Status;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Request payload for updating an existing site assessment.
 * All fields are optional; only non-null values are applied.
 */
public record UpdateSiteRequest(
        @Size(max = 100) String name,
        @Min(-90) @Max(90) Double latitude,
        @Min(-180) @Max(180) Double longitude,
        String basin,
        @PositiveOrZero BigDecimal estimatedVolume,
        Confidence confidence,
        Status status
) {}