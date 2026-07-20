package com.demo.siteapi.service;

import com.demo.siteapi.dto.CreateSiteRequest;
import com.demo.siteapi.dto.UpdateSiteRequest;
import com.demo.siteapi.model.SiteAssessment;
import com.demo.siteapi.repository.SiteAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Business service that encapsulates all operations on
 * {@link SiteAssessment} entities.
 * <p>
 * This layer owns the core logic (creation, update merging, deletion)
 * and defines transaction boundaries. The web layer delegates to it,
 * so no business logic remains in controllers.
 */
@Service
@Transactional(readOnly = true)
public class SiteAssessmentService {

    private final SiteAssessmentRepository repository;

    @Autowired
    public SiteAssessmentService(SiteAssessmentRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves every site assessment in the system.
     *
     * @return all assessments (never {@code null})
     */
    public List<SiteAssessment> findAll() {
        return repository.findAll();
    }

    /**
     * Finds a single assessment by its unique identifier.
     *
     * @param id the UUID of the assessment
     * @return the matching assessment
     * @throws NoSuchElementException if no assessment exists with that ID
     */
    public SiteAssessment findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Site not found: " + id));
    }

    /**
     * Creates a new site assessment from the given DTO.
     *
     * @param request validated creation payload
     * @return the persisted assessment
     */
    @Transactional
    public SiteAssessment create(CreateSiteRequest request) {
        SiteAssessment site = new SiteAssessment(
                request.name(), request.latitude(), request.longitude(),
                request.basin(), request.estimatedVolume(),
                request.confidence(), request.status()
        );
        return repository.save(site);
    }

    /**
     * Fully updates an existing assessment.
     * <p>
     * Only non-null fields in {@code request} are applied; all other
     * fields remain unchanged.
     *
     * @param id      the UUID of the assessment to update
     * @param request the update payload (may contain partial data)
     * @return the updated assessment
     * @throws NoSuchElementException if the assessment does not exist
     */
    @Transactional
    public SiteAssessment update(UUID id, UpdateSiteRequest request) {
        SiteAssessment existing = findById(id);
        nullSafeSet(request, existing);
        return repository.save(existing);
    }

    /**
     * Deletes the assessment with the given ID.
     *
     * @param id the UUID of the assessment to delete
     * @throws NoSuchElementException if the assessment does not exist
     */
    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Site not found: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Copies every non-null field from the update request onto the
     * existing entity. This avoids overwriting fields that were not
     * supplied by the client.
     *
     * @param request  the incoming update payload
     * @param existing the current state of the entity
     */
    private void nullSafeSet(UpdateSiteRequest request, SiteAssessment existing) {
        if (request.name() != null) existing.setName(request.name());
        if (request.latitude() != null) existing.setLatitude(request.latitude());
        if (request.longitude() != null) existing.setLongitude(request.longitude());
        if (request.basin() != null) existing.setBasin(request.basin());
        if (request.estimatedVolume() != null) existing.setEstimatedVolume(request.estimatedVolume());
        if (request.confidence() != null) existing.setConfidence(request.confidence());
        if (request.status() != null) existing.setStatus(request.status());
    }
}