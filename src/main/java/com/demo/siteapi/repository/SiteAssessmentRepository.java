package com.demo.siteapi.repository;

import com.demo.siteapi.model.SiteAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link SiteAssessment} entities.
 */
public interface SiteAssessmentRepository extends JpaRepository<SiteAssessment, UUID> {}