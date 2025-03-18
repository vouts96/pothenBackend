package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Submission;
import com.mycompany.myapp.repository.SubmissionRepository;
import com.mycompany.myapp.service.SubmissionService;
import com.mycompany.myapp.service.dto.SubmissionDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Submission}.
 */
@RestController
@RequestMapping("/api/submissions")
public class SubmissionResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubmissionResource.class);

    private static final String ENTITY_NAME = "submission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubmissionService submissionService;

    private final SubmissionRepository submissionRepository;

    public SubmissionResource(SubmissionService submissionService, SubmissionRepository submissionRepository) {
        this.submissionService = submissionService;
        this.submissionRepository = submissionRepository;
    }

    /**
     * {@code POST  /submissions} : Create a new submission.
     *
     * @param submissionDTO the submissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new submissionDTO, or with status {@code 400 (Bad Request)} if the submission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubmissionDTO> createSubmission(@Valid @RequestBody SubmissionDTO submissionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Submission : {}", submissionDTO);
        if (submissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new submission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        submissionDTO = submissionService.save(submissionDTO);
        return ResponseEntity.created(new URI("/api/submissions/" + submissionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, submissionDTO.getId().toString()))
            .body(submissionDTO);
    }

    /**
     * {@code PUT  /submissions/:id} : Updates an existing submission.
     *
     * @param id the id of the submissionDTO to save.
     * @param submissionDTO the submissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submissionDTO,
     * or with status {@code 400 (Bad Request)} if the submissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the submissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubmissionDTO> updateSubmission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubmissionDTO submissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Submission : {}, {}", id, submissionDTO);
        if (submissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, submissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!submissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        submissionDTO = submissionService.update(submissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, submissionDTO.getId().toString()))
            .body(submissionDTO);
    }

    /**
     * {@code PATCH  /submissions/:id} : Partial updates given fields of an existing submission, field will ignore if it is null
     *
     * @param id the id of the submissionDTO to save.
     * @param submissionDTO the submissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submissionDTO,
     * or with status {@code 400 (Bad Request)} if the submissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the submissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the submissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<SubmissionDTO> partialUpdateSubmission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubmissionDTO submissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Submission partially : {}, {}", id, submissionDTO);
        if (submissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, submissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!submissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubmissionDTO> result = submissionService.partialUpdate(submissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, submissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /submissions} : get all the submissions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submissions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubmissionDTO>> getAllSubmissions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Submissions");

        // Get the authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        // Check if the user has the ADMIN role
        boolean isAdmin = authentication
            .getAuthorities()
            .stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        Page<SubmissionDTO> page;
        if (isAdmin) {
            // Admin can view all submissions
            page = submissionService.findAll(pageable);
        } else {
            // Regular users can only view their own submissions
            page = submissionService.findAllByUser(pageable, login);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /submissions/:id} : get the "id" submission.
     *
     * @param id the id of the submissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the submissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Submission : {}", id);
        Optional<SubmissionDTO> submissionDTO = submissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(submissionDTO);
    }

    /**
     * {@code DELETE  /submissions/:id} : delete the "id" submission.
     *
     * @param id the id of the submissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteSubmission(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Submission : {}", id);
        submissionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
