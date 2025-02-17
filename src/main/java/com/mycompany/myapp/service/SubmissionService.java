package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Submission;
import com.mycompany.myapp.repository.SubmissionRepository;
import com.mycompany.myapp.service.dto.SubmissionDTO;
import com.mycompany.myapp.service.mapper.SubmissionMapper;
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

    public SubmissionService(SubmissionRepository submissionRepository, SubmissionMapper submissionMapper) {
        this.submissionRepository = submissionRepository;
        this.submissionMapper = submissionMapper;
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
        Submission submission = submissionMapper.toEntity(submissionDTO);
        submission = submissionRepository.save(submission);
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
