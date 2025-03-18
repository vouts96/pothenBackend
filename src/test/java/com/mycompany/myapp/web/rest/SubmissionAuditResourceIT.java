package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SubmissionAuditAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SubmissionAudit;
import com.mycompany.myapp.repository.SubmissionAuditRepository;
import com.mycompany.myapp.service.dto.SubmissionAuditDTO;
import com.mycompany.myapp.service.mapper.SubmissionAuditMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SubmissionAuditResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubmissionAuditResourceIT {

    private static final String DEFAULT_AFM = "247920704";
    private static final String UPDATED_AFM = "164031447";

    private static final String DEFAULT_ADT = "AAAAAAAAAA";
    private static final String UPDATED_ADT = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FATHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FATHER_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ACQUISITION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACQUISITION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LOSS_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOSS_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ORGANIZATION_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_ORGANIZATION_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_NEW_ORGANIZATION_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_PROTOCOL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PROTOCOL_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DECISION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DECISION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_PREVIOUS_SUBMISSION = false;
    private static final Boolean UPDATED_PREVIOUS_SUBMISSION = true;

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/submission-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubmissionAuditRepository submissionAuditRepository;

    @Autowired
    private SubmissionAuditMapper submissionAuditMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubmissionAuditMockMvc;

    private SubmissionAudit submissionAudit;

    private SubmissionAudit insertedSubmissionAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubmissionAudit createEntity() {
        return new SubmissionAudit()
            .afm(DEFAULT_AFM)
            .adt(DEFAULT_ADT)
            .lastName(DEFAULT_LAST_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .fatherName(DEFAULT_FATHER_NAME)
            .acquisitionDate(DEFAULT_ACQUISITION_DATE)
            .lossDate(DEFAULT_LOSS_DATE)
            .organizationUnit(DEFAULT_ORGANIZATION_UNIT)
            .newOrganizationUnit(DEFAULT_NEW_ORGANIZATION_UNIT)
            .protocolNumber(DEFAULT_PROTOCOL_NUMBER)
            .decisionDate(DEFAULT_DECISION_DATE)
            .previousSubmission(DEFAULT_PREVIOUS_SUBMISSION)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .changeType(DEFAULT_CHANGE_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubmissionAudit createUpdatedEntity() {
        return new SubmissionAudit()
            .afm(UPDATED_AFM)
            .adt(UPDATED_ADT)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .acquisitionDate(UPDATED_ACQUISITION_DATE)
            .lossDate(UPDATED_LOSS_DATE)
            .organizationUnit(UPDATED_ORGANIZATION_UNIT)
            .newOrganizationUnit(UPDATED_NEW_ORGANIZATION_UNIT)
            .protocolNumber(UPDATED_PROTOCOL_NUMBER)
            .decisionDate(UPDATED_DECISION_DATE)
            .previousSubmission(UPDATED_PREVIOUS_SUBMISSION)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .changeType(UPDATED_CHANGE_TYPE);
    }

    @BeforeEach
    public void initTest() {
        submissionAudit = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubmissionAudit != null) {
            submissionAuditRepository.delete(insertedSubmissionAudit);
            insertedSubmissionAudit = null;
        }
    }

    @Test
    @Transactional
    void createSubmissionAudit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubmissionAudit
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);
        var returnedSubmissionAuditDTO = om.readValue(
            restSubmissionAuditMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubmissionAuditDTO.class
        );

        // Validate the SubmissionAudit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubmissionAudit = submissionAuditMapper.toEntity(returnedSubmissionAuditDTO);
        assertSubmissionAuditUpdatableFieldsEquals(returnedSubmissionAudit, getPersistedSubmissionAudit(returnedSubmissionAudit));

        insertedSubmissionAudit = returnedSubmissionAudit;
    }

    @Test
    @Transactional
    void createSubmissionAuditWithExistingId() throws Exception {
        // Create the SubmissionAudit with an existing ID
        submissionAudit.setId(1L);
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubmissionAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAfmIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setAfm(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setAdt(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setLastName(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setFirstName(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFatherNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setFatherName(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAcquisitionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setAcquisitionDate(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrganizationUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setOrganizationUnit(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProtocolNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setProtocolNumber(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDecisionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setDecisionDate(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPreviousSubmissionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setPreviousSubmission(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setModifiedDate(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setModifiedBy(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChangeTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submissionAudit.setChangeType(null);

        // Create the SubmissionAudit, which fails.
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        restSubmissionAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubmissionAudits() throws Exception {
        // Initialize the database
        insertedSubmissionAudit = submissionAuditRepository.saveAndFlush(submissionAudit);

        // Get all the submissionAuditList
        restSubmissionAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submissionAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].afm").value(hasItem(DEFAULT_AFM)))
            .andExpect(jsonPath("$.[*].adt").value(hasItem(DEFAULT_ADT)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].acquisitionDate").value(hasItem(DEFAULT_ACQUISITION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lossDate").value(hasItem(DEFAULT_LOSS_DATE.toString())))
            .andExpect(jsonPath("$.[*].organizationUnit").value(hasItem(DEFAULT_ORGANIZATION_UNIT)))
            .andExpect(jsonPath("$.[*].newOrganizationUnit").value(hasItem(DEFAULT_NEW_ORGANIZATION_UNIT)))
            .andExpect(jsonPath("$.[*].protocolNumber").value(hasItem(DEFAULT_PROTOCOL_NUMBER)))
            .andExpect(jsonPath("$.[*].decisionDate").value(hasItem(DEFAULT_DECISION_DATE.toString())))
            .andExpect(jsonPath("$.[*].previousSubmission").value(hasItem(DEFAULT_PREVIOUS_SUBMISSION.booleanValue())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].changeType").value(hasItem(DEFAULT_CHANGE_TYPE)));
    }

    @Test
    @Transactional
    void getSubmissionAudit() throws Exception {
        // Initialize the database
        insertedSubmissionAudit = submissionAuditRepository.saveAndFlush(submissionAudit);

        // Get the submissionAudit
        restSubmissionAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, submissionAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(submissionAudit.getId().intValue()))
            .andExpect(jsonPath("$.afm").value(DEFAULT_AFM))
            .andExpect(jsonPath("$.adt").value(DEFAULT_ADT))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.fatherName").value(DEFAULT_FATHER_NAME))
            .andExpect(jsonPath("$.acquisitionDate").value(DEFAULT_ACQUISITION_DATE.toString()))
            .andExpect(jsonPath("$.lossDate").value(DEFAULT_LOSS_DATE.toString()))
            .andExpect(jsonPath("$.organizationUnit").value(DEFAULT_ORGANIZATION_UNIT))
            .andExpect(jsonPath("$.newOrganizationUnit").value(DEFAULT_NEW_ORGANIZATION_UNIT))
            .andExpect(jsonPath("$.protocolNumber").value(DEFAULT_PROTOCOL_NUMBER))
            .andExpect(jsonPath("$.decisionDate").value(DEFAULT_DECISION_DATE.toString()))
            .andExpect(jsonPath("$.previousSubmission").value(DEFAULT_PREVIOUS_SUBMISSION.booleanValue()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.changeType").value(DEFAULT_CHANGE_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingSubmissionAudit() throws Exception {
        // Get the submissionAudit
        restSubmissionAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubmissionAudit() throws Exception {
        // Initialize the database
        insertedSubmissionAudit = submissionAuditRepository.saveAndFlush(submissionAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the submissionAudit
        SubmissionAudit updatedSubmissionAudit = submissionAuditRepository.findById(submissionAudit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubmissionAudit are not directly saved in db
        em.detach(updatedSubmissionAudit);
        updatedSubmissionAudit
            .afm(UPDATED_AFM)
            .adt(UPDATED_ADT)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .acquisitionDate(UPDATED_ACQUISITION_DATE)
            .lossDate(UPDATED_LOSS_DATE)
            .organizationUnit(UPDATED_ORGANIZATION_UNIT)
            .newOrganizationUnit(UPDATED_NEW_ORGANIZATION_UNIT)
            .protocolNumber(UPDATED_PROTOCOL_NUMBER)
            .decisionDate(UPDATED_DECISION_DATE)
            .previousSubmission(UPDATED_PREVIOUS_SUBMISSION)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .changeType(UPDATED_CHANGE_TYPE);
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(updatedSubmissionAudit);

        restSubmissionAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(submissionAuditDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubmissionAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubmissionAuditToMatchAllProperties(updatedSubmissionAudit);
    }

    @Test
    @Transactional
    void putNonExistingSubmissionAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submissionAudit.setId(longCount.incrementAndGet());

        // Create the SubmissionAudit
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(submissionAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubmissionAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubmissionAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submissionAudit.setId(longCount.incrementAndGet());

        // Create the SubmissionAudit
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(submissionAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubmissionAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubmissionAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submissionAudit.setId(longCount.incrementAndGet());

        // Create the SubmissionAudit
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionAuditMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubmissionAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubmissionAuditWithPatch() throws Exception {
        // Initialize the database
        insertedSubmissionAudit = submissionAuditRepository.saveAndFlush(submissionAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the submissionAudit using partial update
        SubmissionAudit partialUpdatedSubmissionAudit = new SubmissionAudit();
        partialUpdatedSubmissionAudit.setId(submissionAudit.getId());

        partialUpdatedSubmissionAudit
            .adt(UPDATED_ADT)
            .fatherName(UPDATED_FATHER_NAME)
            .organizationUnit(UPDATED_ORGANIZATION_UNIT)
            .decisionDate(UPDATED_DECISION_DATE)
            .previousSubmission(UPDATED_PREVIOUS_SUBMISSION)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .changeType(UPDATED_CHANGE_TYPE);

        restSubmissionAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmissionAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubmissionAudit))
            )
            .andExpect(status().isOk());

        // Validate the SubmissionAudit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubmissionAuditUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubmissionAudit, submissionAudit),
            getPersistedSubmissionAudit(submissionAudit)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubmissionAuditWithPatch() throws Exception {
        // Initialize the database
        insertedSubmissionAudit = submissionAuditRepository.saveAndFlush(submissionAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the submissionAudit using partial update
        SubmissionAudit partialUpdatedSubmissionAudit = new SubmissionAudit();
        partialUpdatedSubmissionAudit.setId(submissionAudit.getId());

        partialUpdatedSubmissionAudit
            .afm(UPDATED_AFM)
            .adt(UPDATED_ADT)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .acquisitionDate(UPDATED_ACQUISITION_DATE)
            .lossDate(UPDATED_LOSS_DATE)
            .organizationUnit(UPDATED_ORGANIZATION_UNIT)
            .newOrganizationUnit(UPDATED_NEW_ORGANIZATION_UNIT)
            .protocolNumber(UPDATED_PROTOCOL_NUMBER)
            .decisionDate(UPDATED_DECISION_DATE)
            .previousSubmission(UPDATED_PREVIOUS_SUBMISSION)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .changeType(UPDATED_CHANGE_TYPE);

        restSubmissionAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmissionAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubmissionAudit))
            )
            .andExpect(status().isOk());

        // Validate the SubmissionAudit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubmissionAuditUpdatableFieldsEquals(
            partialUpdatedSubmissionAudit,
            getPersistedSubmissionAudit(partialUpdatedSubmissionAudit)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSubmissionAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submissionAudit.setId(longCount.incrementAndGet());

        // Create the SubmissionAudit
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, submissionAuditDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(submissionAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubmissionAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubmissionAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submissionAudit.setId(longCount.incrementAndGet());

        // Create the SubmissionAudit
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(submissionAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubmissionAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubmissionAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submissionAudit.setId(longCount.incrementAndGet());

        // Create the SubmissionAudit
        SubmissionAuditDTO submissionAuditDTO = submissionAuditMapper.toDto(submissionAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionAuditMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(submissionAuditDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubmissionAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubmissionAudit() throws Exception {
        // Initialize the database
        insertedSubmissionAudit = submissionAuditRepository.saveAndFlush(submissionAudit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the submissionAudit
        restSubmissionAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, submissionAudit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return submissionAuditRepository.count();
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

    protected SubmissionAudit getPersistedSubmissionAudit(SubmissionAudit submissionAudit) {
        return submissionAuditRepository.findById(submissionAudit.getId()).orElseThrow();
    }

    protected void assertPersistedSubmissionAuditToMatchAllProperties(SubmissionAudit expectedSubmissionAudit) {
        assertSubmissionAuditAllPropertiesEquals(expectedSubmissionAudit, getPersistedSubmissionAudit(expectedSubmissionAudit));
    }

    protected void assertPersistedSubmissionAuditToMatchUpdatableProperties(SubmissionAudit expectedSubmissionAudit) {
        assertSubmissionAuditAllUpdatablePropertiesEquals(expectedSubmissionAudit, getPersistedSubmissionAudit(expectedSubmissionAudit));
    }
}
