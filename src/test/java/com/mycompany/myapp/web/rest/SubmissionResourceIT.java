package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SubmissionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Submission;
import com.mycompany.myapp.repository.SubmissionRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.SubmissionService;
import com.mycompany.myapp.service.dto.SubmissionDTO;
import com.mycompany.myapp.service.mapper.SubmissionMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubmissionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SubmissionResourceIT {

    private static final String DEFAULT_AFM = "317524887";
    private static final String UPDATED_AFM = "301246641";

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

    private static final String ENTITY_API_URL = "/api/submissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SubmissionRepository submissionRepositoryMock;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Mock
    private SubmissionService submissionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubmissionMockMvc;

    private Submission submission;

    private Submission insertedSubmission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createEntity() {
        return new Submission()
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
            .previousSubmission(DEFAULT_PREVIOUS_SUBMISSION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createUpdatedEntity() {
        return new Submission()
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
            .previousSubmission(UPDATED_PREVIOUS_SUBMISSION);
    }

    @BeforeEach
    public void initTest() {
        submission = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubmission != null) {
            submissionRepository.delete(insertedSubmission);
            insertedSubmission = null;
        }
    }

    @Test
    @Transactional
    void createSubmission() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);
        var returnedSubmissionDTO = om.readValue(
            restSubmissionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubmissionDTO.class
        );

        // Validate the Submission in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubmission = submissionMapper.toEntity(returnedSubmissionDTO);
        assertSubmissionUpdatableFieldsEquals(returnedSubmission, getPersistedSubmission(returnedSubmission));

        insertedSubmission = returnedSubmission;
    }

    @Test
    @Transactional
    void createSubmissionWithExistingId() throws Exception {
        // Create the Submission with an existing ID
        submission.setId(1L);
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAfmIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setAfm(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setAdt(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setLastName(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setFirstName(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFatherNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setFatherName(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAcquisitionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setAcquisitionDate(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrganizationUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setOrganizationUnit(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProtocolNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setProtocolNumber(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDecisionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setDecisionDate(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPreviousSubmissionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setPreviousSubmission(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubmissions() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        // Get all the submissionList
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submission.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].previousSubmission").value(hasItem(DEFAULT_PREVIOUS_SUBMISSION.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubmissionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(submissionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubmissionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(submissionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubmissionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(submissionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubmissionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(submissionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSubmission() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        // Get the submission
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL_ID, submission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(submission.getId().intValue()))
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
            .andExpect(jsonPath("$.previousSubmission").value(DEFAULT_PREVIOUS_SUBMISSION.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSubmission() throws Exception {
        // Get the submission
        restSubmissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubmission() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the submission
        Submission updatedSubmission = submissionRepository.findById(submission.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubmission are not directly saved in db
        em.detach(updatedSubmission);
        updatedSubmission
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
            .previousSubmission(UPDATED_PREVIOUS_SUBMISSION);
        SubmissionDTO submissionDTO = submissionMapper.toDto(updatedSubmission);

        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(submissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubmissionToMatchAllProperties(updatedSubmission);
    }

    @Test
    @Transactional
    void putNonExistingSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubmissionWithPatch() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the submission using partial update
        Submission partialUpdatedSubmission = new Submission();
        partialUpdatedSubmission.setId(submission.getId());

        partialUpdatedSubmission
            .afm(UPDATED_AFM)
            .adt(UPDATED_ADT)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .acquisitionDate(UPDATED_ACQUISITION_DATE)
            .lossDate(UPDATED_LOSS_DATE)
            .organizationUnit(UPDATED_ORGANIZATION_UNIT)
            .decisionDate(UPDATED_DECISION_DATE);

        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubmissionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubmission, submission),
            getPersistedSubmission(submission)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubmissionWithPatch() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the submission using partial update
        Submission partialUpdatedSubmission = new Submission();
        partialUpdatedSubmission.setId(submission.getId());

        partialUpdatedSubmission
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
            .previousSubmission(UPDATED_PREVIOUS_SUBMISSION);

        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubmissionUpdatableFieldsEquals(partialUpdatedSubmission, getPersistedSubmission(partialUpdatedSubmission));
    }

    @Test
    @Transactional
    void patchNonExistingSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubmission() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the submission
        restSubmissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, submission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return submissionRepository.count();
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

    protected Submission getPersistedSubmission(Submission submission) {
        return submissionRepository.findById(submission.getId()).orElseThrow();
    }

    protected void assertPersistedSubmissionToMatchAllProperties(Submission expectedSubmission) {
        assertSubmissionAllPropertiesEquals(expectedSubmission, getPersistedSubmission(expectedSubmission));
    }

    protected void assertPersistedSubmissionToMatchUpdatableProperties(Submission expectedSubmission) {
        assertSubmissionAllUpdatablePropertiesEquals(expectedSubmission, getPersistedSubmission(expectedSubmission));
    }
}
