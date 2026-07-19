package com.demo.siteapi.controller;

import com.demo.siteapi.dto.CreateSiteRequest;
import com.demo.siteapi.model.Confidence;
import com.demo.siteapi.model.SiteAssessment;
import com.demo.siteapi.model.Status;
import com.demo.siteapi.repository.SiteAssessmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SiteController.class)
class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SiteAssessmentRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    void getAll_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/sites"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void createSite_withValidBody_shouldReturnCreated() throws Exception {
        CreateSiteRequest request = new CreateSiteRequest(
                "Test Site", 30.0, 40.0, "Permian",
                BigDecimal.valueOf(100), Confidence.MEDIUM, Status.DRAFT
        );

        SiteAssessment saved = new SiteAssessment(
                "Test Site", 30.0, 40.0, "Permian",
                BigDecimal.valueOf(100), Confidence.MEDIUM, Status.DRAFT
        );
        saved.setId(UUID.randomUUID());

        when(repository.save(any())).thenReturn(saved);

        mockMvc.perform(post("/api/sites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser
    void createSite_withMissingName_shouldReturnBadRequest() throws Exception {
        CreateSiteRequest request = new CreateSiteRequest(
                "", 30.0, 40.0, "Permian",
                BigDecimal.valueOf(100), Confidence.MEDIUM, Status.DRAFT
        );

        mockMvc.perform(post("/api/sites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("validation-error"));
    }

    @Test
    void getAll_withoutAuth_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/sites"))
                .andExpect(status().isUnauthorized());
    }
}