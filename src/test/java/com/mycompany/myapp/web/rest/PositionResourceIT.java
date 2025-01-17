package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PositionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Position;
import com.mycompany.myapp.repository.PositionRepository;
import com.mycompany.myapp.service.dto.PositionDTO;
import com.mycompany.myapp.service.mapper.PositionMapper;
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
 * Integration tests for the {@link PositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PositionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/positions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPositionMockMvc;

    private Position position;

    private Position insertedPosition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createEntity() {
        return new Position().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createUpdatedEntity() {
        return new Position().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        position = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPosition != null) {
            positionRepository.delete(insertedPosition);
            insertedPosition = null;
        }
    }

    @Test
    @Transactional
    void createPosition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);
        var returnedPositionDTO = om.readValue(
            restPositionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PositionDTO.class
        );

        // Validate the Position in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPosition = positionMapper.toEntity(returnedPositionDTO);
        assertPositionUpdatableFieldsEquals(returnedPosition, getPersistedPosition(returnedPosition));

        insertedPosition = returnedPosition;
    }

    @Test
    @Transactional
    void createPositionWithExistingId() throws Exception {
        // Create the Position with an existing ID
        position.setId(1L);
        PositionDTO positionDTO = positionMapper.toDto(position);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setName(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositions() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPosition() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get the position
        restPositionMockMvc
            .perform(get(ENTITY_API_URL_ID, position.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(position.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPosition() throws Exception {
        // Get the position
        restPositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPosition() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position
        Position updatedPosition = positionRepository.findById(position.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPosition are not directly saved in db
        em.detach(updatedPosition);
        updatedPosition.name(UPDATED_NAME);
        PositionDTO positionDTO = positionMapper.toDto(updatedPosition);

        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPositionToMatchAllProperties(updatedPosition);
    }

    @Test
    @Transactional
    void putNonExistingPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionWithPatch() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position using partial update
        Position partialUpdatedPosition = new Position();
        partialUpdatedPosition.setId(position.getId());

        partialUpdatedPosition.name(UPDATED_NAME);

        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosition))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPosition, position), getPersistedPosition(position));
    }

    @Test
    @Transactional
    void fullUpdatePositionWithPatch() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position using partial update
        Position partialUpdatedPosition = new Position();
        partialUpdatedPosition.setId(position.getId());

        partialUpdatedPosition.name(UPDATED_NAME);

        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosition))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionUpdatableFieldsEquals(partialUpdatedPosition, getPersistedPosition(partialUpdatedPosition));
    }

    @Test
    @Transactional
    void patchNonExistingPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePosition() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the position
        restPositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, position.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return positionRepository.count();
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

    protected Position getPersistedPosition(Position position) {
        return positionRepository.findById(position.getId()).orElseThrow();
    }

    protected void assertPersistedPositionToMatchAllProperties(Position expectedPosition) {
        assertPositionAllPropertiesEquals(expectedPosition, getPersistedPosition(expectedPosition));
    }

    protected void assertPersistedPositionToMatchUpdatableProperties(Position expectedPosition) {
        assertPositionAllUpdatablePropertiesEquals(expectedPosition, getPersistedPosition(expectedPosition));
    }
}
