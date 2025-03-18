package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SubmissionAudit;
import com.mycompany.myapp.repository.SubmissionAuditRepository;
import com.mycompany.myapp.service.dto.SubmissionAuditDTO;
import com.mycompany.myapp.service.mapper.SubmissionAuditMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.SubmissionAudit}.
 */
@Service
@Transactional
public class SubmissionAuditService {

    private static final Logger LOG = LoggerFactory.getLogger(SubmissionAuditService.class);

    private final SubmissionAuditRepository submissionAuditRepository;

    private final SubmissionAuditMapper submissionAuditMapper;

    public SubmissionAuditService(SubmissionAuditRepository submissionAuditRepository, SubmissionAuditMapper submissionAuditMapper) {
        this.submissionAuditRepository = submissionAuditRepository;
        this.submissionAuditMapper = submissionAuditMapper;
    }

    /**
     * Save a submissionAudit.
     *
     * @param submissionAuditDTO the entity to save.
     * @return the persisted entity.
     */
    public SubmissionAuditDTO save(SubmissionAuditDTO submissionAuditDTO) {
        LOG.debug("Request to save SubmissionAudit : {}", submissionAuditDTO);
        SubmissionAudit submissionAudit = submissionAuditMapper.toEntity(submissionAuditDTO);
        submissionAudit = submissionAuditRepository.save(submissionAudit);
        return submissionAuditMapper.toDto(submissionAudit);
    }

    /**
     * Update a submissionAudit.
     *
     * @param submissionAuditDTO the entity to save.
     * @return the persisted entity.
     */
    public SubmissionAuditDTO update(SubmissionAuditDTO submissionAuditDTO) {
        LOG.debug("Request to update SubmissionAudit : {}", submissionAuditDTO);
        SubmissionAudit submissionAudit = submissionAuditMapper.toEntity(submissionAuditDTO);
        submissionAudit = submissionAuditRepository.save(submissionAudit);
        return submissionAuditMapper.toDto(submissionAudit);
    }

    /**
     * Partially update a submissionAudit.
     *
     * @param submissionAuditDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubmissionAuditDTO> partialUpdate(SubmissionAuditDTO submissionAuditDTO) {
        LOG.debug("Request to partially update SubmissionAudit : {}", submissionAuditDTO);

        return submissionAuditRepository
            .findById(submissionAuditDTO.getId())
            .map(existingSubmissionAudit -> {
                submissionAuditMapper.partialUpdate(existingSubmissionAudit, submissionAuditDTO);

                return existingSubmissionAudit;
            })
            .map(submissionAuditRepository::save)
            .map(submissionAuditMapper::toDto);
    }

    /**
     * Get all the submissionAudits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubmissionAuditDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SubmissionAudits");
        return submissionAuditRepository.findAll(pageable).map(submissionAuditMapper::toDto);
    }

    /**
     * Get one submissionAudit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubmissionAuditDTO> findOne(Long id) {
        LOG.debug("Request to get SubmissionAudit : {}", id);
        return submissionAuditRepository.findById(id).map(submissionAuditMapper::toDto);
    }

    /**
     * Delete the submissionAudit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SubmissionAudit : {}", id);
        submissionAuditRepository.deleteById(id);
    }
}
