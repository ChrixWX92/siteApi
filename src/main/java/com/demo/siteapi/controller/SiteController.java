package com.demo.siteapi.controller;

import com.demo.siteapi.dto.CreateSiteRequest;
import com.demo.siteapi.dto.UpdateSiteRequest;
import com.demo.siteapi.model.SiteAssessment;
import com.demo.siteapi.repository.SiteAssessmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * REST controller for managing site assessments.
 * All endpoints under /api/sites require authentication.
 */
@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private static final Logger log = LoggerFactory.getLogger(SiteController.class);
    private final SiteAssessmentRepository repository;

    public SiteController(SiteAssessmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "List all site assessments")
    public List<SiteAssessment> getAll() {
        log.info("Fetching all site assessments");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a site assessment by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    public SiteAssessment getById(@PathVariable UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Site not found: " + id));
    }

    @PostMapping
    @Operation(summary = "Create a new site assessment")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<SiteAssessment> create(@Valid @RequestBody CreateSiteRequest request) {
        log.info("Creating site: {}", request.name());
        SiteAssessment site = new SiteAssessment(
                request.name(), request.latitude(), request.longitude(),
                request.basin(), request.estimatedVolume(),
                request.confidence(), request.status()
        );
        SiteAssessment saved = repository.save(site);
        return ResponseEntity.created(URI.create("/api/sites/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing site assessment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Updated"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public SiteAssessment update(@PathVariable UUID id, @Valid @RequestBody UpdateSiteRequest request) {
        SiteAssessment existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Site not found: " + id));
        if (request.name() != null) existing.setName(request.name());
        if (request.latitude() != null) existing.setLatitude(request.latitude());
        if (request.longitude() != null) existing.setLongitude(request.longitude());
        if (request.basin() != null) existing.setBasin(request.basin());
        if (request.estimatedVolume() != null) existing.setEstimatedVolume(request.estimatedVolume());
        if (request.confidence() != null) existing.setConfidence(request.confidence());
        if (request.status() != null) existing.setStatus(request.status());
        log.info("Updating site {}", id);
        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a site assessment")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Deleted"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Site not found: " + id);
        }
        repository.deleteById(id);
        log.info("Deleted site {}", id);
        return ResponseEntity.noContent().build();
    }
}