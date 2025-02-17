package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CommitteeRepository;
import com.mycompany.myapp.service.CommitteeService;
import com.mycompany.myapp.service.dto.CommitteeDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Committee}.
 */
@RestController
@RequestMapping("/api/committees")
public class CommitteeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommitteeResource.class);

    private static final String ENTITY_NAME = "committee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommitteeService committeeService;

    private final CommitteeRepository committeeRepository;

    public CommitteeResource(CommitteeService committeeService, CommitteeRepository committeeRepository) {
        this.committeeService = committeeService;
        this.committeeRepository = committeeRepository;
    }

    /**
     * {@code POST  /committees} : Create a new committee.
     *
     * @param committeeDTO the committeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new committeeDTO, or with status {@code 400 (Bad Request)} if the committee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommitteeDTO> createCommittee(@Valid @RequestBody CommitteeDTO committeeDTO) throws URISyntaxException {
        LOG.debug("REST request to save Committee : {}", committeeDTO);
        if (committeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new committee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        committeeDTO = committeeService.save(committeeDTO);
        return ResponseEntity.created(new URI("/api/committees/" + committeeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, committeeDTO.getId().toString()))
            .body(committeeDTO);
    }

    /**
     * {@code PUT  /committees/:id} : Updates an existing committee.
     *
     * @param id the id of the committeeDTO to save.
     * @param committeeDTO the committeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated committeeDTO,
     * or with status {@code 400 (Bad Request)} if the committeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the committeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommitteeDTO> updateCommittee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommitteeDTO committeeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Committee : {}, {}", id, committeeDTO);
        if (committeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, committeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!committeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        committeeDTO = committeeService.update(committeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, committeeDTO.getId().toString()))
            .body(committeeDTO);
    }

    /**
     * {@code PATCH  /committees/:id} : Partial updates given fields of an existing committee, field will ignore if it is null
     *
     * @param id the id of the committeeDTO to save.
     * @param committeeDTO the committeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated committeeDTO,
     * or with status {@code 400 (Bad Request)} if the committeeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the committeeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the committeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommitteeDTO> partialUpdateCommittee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommitteeDTO committeeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Committee partially : {}, {}", id, committeeDTO);
        if (committeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, committeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!committeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommitteeDTO> result = committeeService.partialUpdate(committeeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, committeeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /committees} : get all the committees.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of committees in body.
     */
    @GetMapping("")
    public List<CommitteeDTO> getAllCommittees() {
        LOG.debug("REST request to get all Committees");
        return committeeService.findAll();
    }

    /**
     * {@code GET  /committees/:id} : get the "id" committee.
     *
     * @param id the id of the committeeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the committeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommitteeDTO> getCommittee(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Committee : {}", id);
        Optional<CommitteeDTO> committeeDTO = committeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(committeeDTO);
    }

    /**
     * {@code DELETE  /committees/:id} : delete the "id" committee.
     *
     * @param id the id of the committeeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCommittee(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Committee : {}", id);
        committeeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
