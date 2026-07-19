package com.demo.siteapi.dto;

import com.demo.siteapi.model.Confidence;
import com.demo.siteapi.model.Status;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Request payload for creating a new site assessment.
 * All fields are mandatory except estimatedVolume.
 */
public record CreateSiteRequest(
        @NotBlank @Size(max = 100) String name,
        @Min(-90) @Max(90) double latitude,
        @Min(-180) @Max(180) double longitude,
        @NotBlank String basin,
        @PositiveOrZero BigDecimal estimatedVolume,
        @NotNull Confidence confidence,
        @NotNull Status status
) {}