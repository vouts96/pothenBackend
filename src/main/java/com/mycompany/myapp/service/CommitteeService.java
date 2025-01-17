package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Committee;
import com.mycompany.myapp.repository.CommitteeRepository;
import com.mycompany.myapp.service.dto.CommitteeDTO;
import com.mycompany.myapp.service.mapper.CommitteeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Committee}.
 */
@Service
@Transactional
public class CommitteeService {

    private static final Logger LOG = LoggerFactory.getLogger(CommitteeService.class);

    private final CommitteeRepository committeeRepository;

    private final CommitteeMapper committeeMapper;

    public CommitteeService(CommitteeRepository committeeRepository, CommitteeMapper committeeMapper) {
        this.committeeRepository = committeeRepository;
        this.committeeMapper = committeeMapper;
    }

    /**
     * Save a committee.
     *
     * @param committeeDTO the entity to save.
     * @return the persisted entity.
     */
    public CommitteeDTO save(CommitteeDTO committeeDTO) {
        LOG.debug("Request to save Committee : {}", committeeDTO);
        Committee committee = committeeMapper.toEntity(committeeDTO);
        committee = committeeRepository.save(committee);
        return committeeMapper.toDto(committee);
    }

    /**
     * Update a committee.
     *
     * @param committeeDTO the entity to save.
     * @return the persisted entity.
     */
    public CommitteeDTO update(CommitteeDTO committeeDTO) {
        LOG.debug("Request to update Committee : {}", committeeDTO);
        Committee committee = committeeMapper.toEntity(committeeDTO);
        committee = committeeRepository.save(committee);
        return committeeMapper.toDto(committee);
    }

    /**
     * Partially update a committee.
     *
     * @param committeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommitteeDTO> partialUpdate(CommitteeDTO committeeDTO) {
        LOG.debug("Request to partially update Committee : {}", committeeDTO);

        return committeeRepository
            .findById(committeeDTO.getId())
            .map(existingCommittee -> {
                committeeMapper.partialUpdate(existingCommittee, committeeDTO);

                return existingCommittee;
            })
            .map(committeeRepository::save)
            .map(committeeMapper::toDto);
    }

    /**
     * Get all the committees.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommitteeDTO> findAll() {
        LOG.debug("Request to get all Committees");
        return committeeRepository.findAll().stream().map(committeeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one committee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommitteeDTO> findOne(Long id) {
        LOG.debug("Request to get Committee : {}", id);
        return committeeRepository.findById(id).map(committeeMapper::toDto);
    }

    /**
     * Delete the committee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Committee : {}", id);
        committeeRepository.deleteById(id);
    }
}
