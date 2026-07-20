package com.demo.siteapi.controller;

import com.demo.siteapi.dto.CreateSiteRequest;
import com.demo.siteapi.dto.UpdateSiteRequest;
import com.demo.siteapi.model.SiteAssessment;
import com.demo.siteapi.service.SiteAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST controller that exposes site assessment operations.
 * <p>
 * All endpoints under {@code /api/sites} require HTTP Basic
 * authentication (configured in {@code SecurityConfig}).
 * The controller is a thin adapter: it delegates business
 * logic to {@link SiteAssessmentService} and handles HTTP
 * concerns (status codes, location headers, logging).
 */
@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private static final Logger log = LoggerFactory.getLogger(SiteController.class);
    private final SiteAssessmentService service;

    @Autowired
    public SiteController(SiteAssessmentService service) {
        this.service = service;
    }

    /**
     * Lists every site assessment.
     *
     * @return 200 OK with a JSON array
     */
    @GetMapping
    @Operation(summary = "List all site assessments")
    public List<SiteAssessment> getAll() {
        log.info("Fetching all site assessments");
        return service.findAll();
    }

    /**
     * Retrieves a single assessment by ID.
     *
     * @param id the UUID of the assessment
     * @return 200 OK with the assessment, or 404
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a site assessment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public SiteAssessment getById(@PathVariable UUID id) {
        return service.findById(id);
    }

    /**
     * Creates a new assessment.
     *
     * @param request validated creation payload
     * @return 201 Created with a {@code Location} header and the new resource
     */
    @PostMapping
    @Operation(summary = "Create a new site assessment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<SiteAssessment> create(@Valid @RequestBody CreateSiteRequest request) {
        log.info("Creating site: {}", request.name());
        SiteAssessment saved = service.create(request);
        return ResponseEntity.created(URI.create("/api/sites/" + saved.getId())).body(saved);
    }

    /**
     * Updates an existing assessment with the supplied fields.
     * <p>
     * Only non-null fields are applied; absent fields keep their
     * existing values.
     *
     * @param id      the UUID of the assessment to update
     * @param request the update payload
     * @return 200 OK with the updated assessment, or 404
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing site assessment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public SiteAssessment update(@PathVariable UUID id, @Valid @RequestBody UpdateSiteRequest request) {
        log.info("Updating site {}", id);
        return service.update(id, request);
    }

    /**
     * Deletes an assessment.
     *
     * @param id the UUID of the assessment to delete
     * @return 204 No Content, or 404
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a site assessment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        log.info("Deleted site {}", id);
        return ResponseEntity.noContent().build();
    }
}