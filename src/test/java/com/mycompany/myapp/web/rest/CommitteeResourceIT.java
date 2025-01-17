package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CommitteeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Committee;
import com.mycompany.myapp.repository.CommitteeRepository;
import com.mycompany.myapp.service.dto.CommitteeDTO;
import com.mycompany.myapp.service.mapper.CommitteeMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CommitteeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommitteeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/committees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private CommitteeMapper committeeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommitteeMockMvc;

    private Committee committee;

    private Committee insertedCommittee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Committee createEntity() {
        return new Committee().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Committee createUpdatedEntity() {
        return new Committee().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        committee = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCommittee != null) {
            committeeRepository.delete(insertedCommittee);
            insertedCommittee = null;
        }
    }

    @Test
    @Transactional
    void createCommittee() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Committee
        CommitteeDTO committeeDTO = committeeMapper.toDto(committee);
        var returnedCommitteeDTO = om.readValue(
            restCommitteeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(committeeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommitteeDTO.class
        );

        // Validate the Committee in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCommittee = committeeMapper.toEntity(returnedCommitteeDTO);
        assertCommitteeUpdatableFieldsEquals(returnedCommittee, getPersistedCommittee(returnedCommittee));

        insertedCommittee = returnedCommittee;
    }

    @Test
    @Transactional
    void createCommitteeWithExistingId() throws Exception {
        // Create the Committee with an existing ID
        committee.setId(1L);
        CommitteeDTO committeeDTO = committeeMapper.toDto(committee);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitteeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(committeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Committee in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        committee.setName(null);

        // Create the Committee, which fails.
        CommitteeDTO committeeDTO = committeeMapper.toDto(committee);

        restCommitteeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(committeeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommittees() throws Exception {
        // Initialize the database
        insertedCommittee = committeeRepository.saveAndFlush(committee);

        // Get all the committeeList
        restCommitteeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committee.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCommittee() throws Exception {
        // Initialize the database
        insertedCommittee = committeeRepository.saveAndFlush(committee);

        // Get the committee
        restCommitteeMockMvc
            .perform(get(ENTITY_API_URL_ID, committee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(committee.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCommittee() throws Exception {
        // Get the committee
        restCommitteeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommittee() throws Exception {
        // Initialize the database
        insertedCommittee = committeeRepository.saveAndFlush(committee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the committee
        Committee updatedCommittee = committeeRepository.findById(committee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommittee are not directly saved in db
        em.detach(updatedCommittee);
        updatedCommittee.name(UPDATED_NAME);
        CommitteeDTO committeeDTO = committeeMapper.toDto(updatedCommittee);

        restCommitteeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, committeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(committeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Committee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommitteeToMatchAllProperties(updatedCommittee);
    }

    @Test
    @Transactional
    void putNonExistingCommittee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        committee.setId(longCount.incrementAndGet());

        // Create the Committee
        CommitteeDTO committeeDTO = committeeMapper.toDto(committee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitteeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, committeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(committeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Committee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommittee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        committee.setId(longCount.incrementAndGet());

        // Create the Committee
        CommitteeDTO committeeDTO = committeeMapper.toDto(committee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitteeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(committeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Committee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommittee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        committee.setId(longCount.incrementAndGet());

        // Create the Committee
        CommitteeDTO committeeDTO = committeeMapper.toDto(committee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitteeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(committeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Committee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommitteeWithPatch() throws Exception {
        // Initialize the database
        insertedCommittee = committeeRepository.saveAndFlush(committee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the committee using partial update
        Committee partialUpdatedCommittee = new Committee();
        partialUpdatedCommittee.setId(committee.getId());

        restCommitteeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommittee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommittee))
            )
            .andExpect(status().isOk());

        // Validate the Committee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommitteeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommittee, committee),
            getPersistedCommittee(committee)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommitteeWithPatch() throws Exception {
        // Initialize the database
        insertedCommittee = committeeRepository.saveAndFlush(committee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the committee using partial update
        Committee partialUpdatedCommittee = new Committee();
        partialUpdatedCommittee.setId(committee.getId());

        partialUpdatedCommittee.name(UPDATED_NAME);

        restCommitteeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommittee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommittee))
            )
            .andExpect(status().isOk());

        // Validate the Committee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommitteeUpdatableFieldsEquals(partialUpdatedCommittee, getPersistedCommittee(partialUpdatedCommittee));
    }

    @Test
    @Transactional
    void patchNonExistingCommittee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        committee.setId(longCount.incrementAndGet());

        // Create the Committee
        CommitteeDTO committeeDTO = committeeMapper.toDto(committee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitteeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, committeeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(committeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Committee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommittee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        committee.setId(longCount.incrementAndGet());

        // Create the Committee
        CommitteeDTO committeeDTO = committeeMapper.toDto(committee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitteeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(committeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Committee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommittee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        committee.setId(longCount.incrementAndGet());

        // Create the Committee
        CommitteeDTO committeeDTO = committeeMapper.toDto(committee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitteeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(committeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Committee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommittee() throws Exception {
        // Initialize the database
        insertedCommittee = committeeRepository.saveAndFlush(committee);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the committee
        restCommitteeMockMvc
            .perform(delete(ENTITY_API_URL_ID, committee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return committeeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Committee getPersistedCommittee(Committee committee) {
        return committeeRepository.findById(committee.getId()).orElseThrow();
    }

    protected void assertPersistedCommitteeToMatchAllProperties(Committee expectedCommittee) {
        assertCommitteeAllPropertiesEquals(expectedCommittee, getPersistedCommittee(expectedCommittee));
    }

    protected void assertPersistedCommitteeToMatchUpdatableProperties(Committee expectedCommittee) {
        assertCommitteeAllUpdatablePropertiesEquals(expectedCommittee, getPersistedCommittee(expectedCommittee));
    }
}
