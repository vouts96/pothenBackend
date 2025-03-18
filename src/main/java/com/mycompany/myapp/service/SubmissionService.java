package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Submission;
import com.mycompany.myapp.domain.SubmissionAudit;
import com.mycompany.myapp.repository.SubmissionAuditRepository;
import com.mycompany.myapp.repository.SubmissionRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.SubmissionDTO;
import com.mycompany.myapp.service.mapper.SubmissionMapper;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Submission}.
 */
@Service
@Transactional
public class SubmissionService {

    private static final Logger LOG = LoggerFactory.getLogger(SubmissionService.class);

    private final SubmissionRepository submissionRepository;

    private final SubmissionMapper submissionMapper;

    private final SubmissionAuditRepository submissionAuditRepository;

    public SubmissionService(
        SubmissionRepository submissionRepository,
        SubmissionMapper submissionMapper,
        SubmissionAuditRepository submissionAuditRepository
    ) {
        this.submissionRepository = submissionRepository;
        this.submissionMapper = submissionMapper;
        this.submissionAuditRepository = submissionAuditRepository;
    }

    /**
     * Save a submission.
     *
     * @param submissionDTO the entity to save.
     * @return the persisted entity.
     */
    public SubmissionDTO save(SubmissionDTO submissionDTO) {
        LOG.debug("Request to save Submission : {}", submissionDTO);
        Submission submission = submissionMapper.toEntity(submissionDTO);
        submission = submissionRepository.save(submission);
        return submissionMapper.toDto(submission);
    }

    /**
     * Update a submission.
     *
     * @param submissionDTO the entity to save.
     * @return the persisted entity.
     */
    public SubmissionDTO update(SubmissionDTO submissionDTO) {
        LOG.debug("Request to update Submission : {}", submissionDTO);

        // Fetch the existing Submission from the database
        Optional<Submission> existingSubmission = submissionRepository.findById(submissionDTO.getId());

        Submission submission = submissionMapper.toEntity(submissionDTO);
        submission = submissionRepository.save(submission);

        // If an existing record was found, create an audit entry
        existingSubmission.ifPresent(oldSubmission -> {
            SubmissionAudit audit = new SubmissionAudit();
            audit.setAfm(oldSubmission.getAfm());
            audit.setAdt(oldSubmission.getAdt());
            audit.setLastName(oldSubmission.getLastName());
            audit.setFirstName(oldSubmission.getFirstName());
            audit.setFatherName(oldSubmission.getFatherName());
            audit.setAcquisitionDate(oldSubmission.getAcquisitionDate());
            audit.setLossDate(oldSubmission.getLossDate());
            audit.setOrganizationUnit(oldSubmission.getOrganizationUnit());
            audit.setNewOrganizationUnit(oldSubmission.getNewOrganizationUnit());
            audit.setProtocolNumber(oldSubmission.getProtocolNumber());
            audit.setDecisionDate(oldSubmission.getDecisionDate());
            audit.setPreviousSubmission(oldSubmission.getPreviousSubmission());
            audit.setModifiedDate(Instant.now());
            audit.setModifiedBy(SecurityUtils.getCurrentUserLogin().orElse("unknown"));
            audit.setChangeType("UPDATE");
            audit.setOriginalSubmission(oldSubmission);

            submissionAuditRepository.save(audit);
        });

        return submissionMapper.toDto(submission);
    }

    /**
     * Partially update a submission.
     *
     * @param submissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubmissionDTO> partialUpdate(SubmissionDTO submissionDTO) {
        LOG.debug("Request to partially update Submission : {}", submissionDTO);

        return submissionRepository
            .findById(submissionDTO.getId())
            .map(existingSubmission -> {
                // Store the previous version in SubmissionAudit
                SubmissionAudit audit = new SubmissionAudit();
                audit.setAfm(existingSubmission.getAfm());
                audit.setAdt(existingSubmission.getAdt());
                audit.setLastName(existingSubmission.getLastName());
                audit.setFirstName(existingSubmission.getFirstName());
                audit.setFatherName(existingSubmission.getFatherName());
                audit.setAcquisitionDate(existingSubmission.getAcquisitionDate());
                audit.setLossDate(existingSubmission.getLossDate());
                audit.setOrganizationUnit(existingSubmission.getOrganizationUnit());
                audit.setNewOrganizationUnit(existingSubmission.getNewOrganizationUnit());
                audit.setProtocolNumber(existingSubmission.getProtocolNumber());
                audit.setDecisionDate(existingSubmission.getDecisionDate());
                audit.setPreviousSubmission(existingSubmission.getPreviousSubmission());
                audit.setModifiedDate(Instant.now());
                audit.setModifiedBy(SecurityUtils.getCurrentUserLogin().orElse("unknown"));
                audit.setChangeType("PARTIAL_UPDATE");
                audit.setOriginalSubmission(existingSubmission);

                submissionAuditRepository.save(audit);

                // Apply the partial update
                submissionMapper.partialUpdate(existingSubmission, submissionDTO);
                return existingSubmission;
            })
            .map(submissionRepository::save)
            .map(submissionMapper::toDto);
    }

    // /**
    //  * Get all the submissions.
    //  *
    //  * @param pageable the pagination information.
    //  * @return the list of entities.
    //  */
    // @Transactional(readOnly = true)
    // public Page<SubmissionDTO> findAll(Pageable pageable) {
    //     LOG.debug("Request to get all Submissions");
    //     return submissionRepository.findAll(pageable).map(submissionMapper::toDto);
    // }

    /**
     * Get all the submissions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SubmissionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return submissionRepository.findAllWithEagerRelationships(pageable).map(submissionMapper::toDto);
    }

    /**
     * Get one submission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubmissionDTO> findOne(Long id) {
        LOG.debug("Request to get Submission : {}", id);
        return submissionRepository.findOneWithEagerRelationships(id).map(submissionMapper::toDto);
    }

    /**
     * Delete the submission by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Submission : {}", id);
        submissionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<SubmissionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Submissions");
        return submissionRepository.findAll(pageable).map(submissionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<SubmissionDTO> findAllByUser(Pageable pageable, String login) {
        LOG.debug("Request to get all Submissions for user: {}", login);
        return submissionRepository.findAllByUserLogin(pageable, login).map(submissionMapper::toDto);
    }
}
