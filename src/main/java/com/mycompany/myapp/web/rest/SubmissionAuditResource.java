package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.SubmissionAuditRepository;
import com.mycompany.myapp.service.SubmissionAuditService;
import com.mycompany.myapp.service.dto.SubmissionAuditDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SubmissionAudit}.
 */
@RestController
@RequestMapping("/api/submission-audits")
public class SubmissionAuditResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubmissionAuditResource.class);

    private static final String ENTITY_NAME = "submissionAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubmissionAuditService submissionAuditService;

    private final SubmissionAuditRepository submissionAuditRepository;

    public SubmissionAuditResource(SubmissionAuditService submissionAuditService, SubmissionAuditRepository submissionAuditRepository) {
        this.submissionAuditService = submissionAuditService;
        this.submissionAuditRepository = submissionAuditRepository;
    }

    /**
     * {@code POST  /submission-audits} : Create a new submissionAudit.
     *
     * @param submissionAuditDTO the submissionAuditDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new submissionAuditDTO, or with status {@code 400 (Bad Request)} if the submissionAudit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubmissionAuditDTO> createSubmissionAudit(@Valid @RequestBody SubmissionAuditDTO submissionAuditDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SubmissionAudit : {}", submissionAuditDTO);
        if (submissionAuditDTO.getId() != null) {
            throw new BadRequestAlertException("A new submissionAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        submissionAuditDTO = submissionAuditService.save(submissionAuditDTO);
        return ResponseEntity.created(new URI("/api/submission-audits/" + submissionAuditDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, submissionAuditDTO.getId().toString()))
            .body(submissionAuditDTO);
    }

    /**
     * {@code PUT  /submission-audits/:id} : Updates an existing submissionAudit.
     *
     * @param id the id of the submissionAuditDTO to save.
     * @param submissionAuditDTO the submissionAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submissionAuditDTO,
     * or with status {@code 400 (Bad Request)} if the submissionAuditDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the submissionAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubmissionAuditDTO> updateSubmissionAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubmissionAuditDTO submissionAuditDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubmissionAudit : {}, {}", id, submissionAuditDTO);
        if (submissionAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, submissionAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!submissionAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        submissionAuditDTO = submissionAuditService.update(submissionAuditDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, submissionAuditDTO.getId().toString()))
            .body(submissionAuditDTO);
    }

    /**
     * {@code PATCH  /submission-audits/:id} : Partial updates given fields of an existing submissionAudit, field will ignore if it is null
     *
     * @param id the id of the submissionAuditDTO to save.
     * @param submissionAuditDTO the submissionAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submissionAuditDTO,
     * or with status {@code 400 (Bad Request)} if the submissionAuditDTO is not valid,
     * or with status {@code 404 (Not Found)} if the submissionAuditDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the submissionAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubmissionAuditDTO> partialUpdateSubmissionAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubmissionAuditDTO submissionAuditDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubmissionAudit partially : {}, {}", id, submissionAuditDTO);
        if (submissionAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, submissionAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!submissionAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubmissionAuditDTO> result = submissionAuditService.partialUpdate(submissionAuditDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, submissionAuditDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /submission-audits} : get all the submissionAudits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submissionAudits in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubmissionAuditDTO>> getAllSubmissionAudits(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of SubmissionAudits");
        Page<SubmissionAuditDTO> page = submissionAuditService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /submission-audits/:id} : get the "id" submissionAudit.
     *
     * @param id the id of the submissionAuditDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the submissionAuditDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubmissionAuditDTO> getSubmissionAudit(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubmissionAudit : {}", id);
        Optional<SubmissionAuditDTO> submissionAuditDTO = submissionAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(submissionAuditDTO);
    }

    /**
     * {@code DELETE  /submission-audits/:id} : delete the "id" submissionAudit.
     *
     * @param id the id of the submissionAuditDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmissionAudit(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubmissionAudit : {}", id);
        submissionAuditService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
