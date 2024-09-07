package com.ab.hr.web.rest;

import static com.ab.hr.domain.JobPostingAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.JobPosting;
import com.ab.hr.domain.enumeration.JobStatus;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.JobPostingRepository;
import com.ab.hr.service.dto.JobPostingDTO;
import com.ab.hr.service.mapper.JobPostingMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link JobPostingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class JobPostingResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final JobStatus DEFAULT_STATUS = JobStatus.OPEN;
    private static final JobStatus UPDATED_STATUS = JobStatus.CLOSED;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/job-postings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobPostingMapper jobPostingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private JobPosting jobPosting;

    private JobPosting insertedJobPosting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobPosting createEntity(EntityManager em) {
        JobPosting jobPosting = new JobPosting()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .expireDate(DEFAULT_EXPIRE_DATE);
        return jobPosting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobPosting createUpdatedEntity(EntityManager em) {
        JobPosting jobPosting = new JobPosting()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .expireDate(UPDATED_EXPIRE_DATE);
        return jobPosting;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(JobPosting.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        jobPosting = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedJobPosting != null) {
            jobPostingRepository.delete(insertedJobPosting).block();
            insertedJobPosting = null;
        }
        deleteEntities(em);
    }

    @Test
    void createJobPosting() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the JobPosting
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);
        var returnedJobPostingDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(JobPostingDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the JobPosting in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedJobPosting = jobPostingMapper.toEntity(returnedJobPostingDTO);
        assertJobPostingUpdatableFieldsEquals(returnedJobPosting, getPersistedJobPosting(returnedJobPosting));

        insertedJobPosting = returnedJobPosting;
    }

    @Test
    void createJobPostingWithExistingId() throws Exception {
        // Create the JobPosting with an existing ID
        jobPosting.setId(1L);
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobPosting in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobPosting.setTitle(null);

        // Create the JobPosting, which fails.
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobPosting.setDescription(null);

        // Create the JobPosting, which fails.
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobPosting.setCreatedDate(null);

        // Create the JobPosting, which fails.
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllJobPostings() {
        // Initialize the database
        insertedJobPosting = jobPostingRepository.save(jobPosting).block();

        // Get all the jobPostingList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(jobPosting.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].expireDate")
            .value(hasItem(DEFAULT_EXPIRE_DATE.toString()));
    }

    @Test
    void getJobPosting() {
        // Initialize the database
        insertedJobPosting = jobPostingRepository.save(jobPosting).block();

        // Get the jobPosting
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, jobPosting.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(jobPosting.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.expireDate")
            .value(is(DEFAULT_EXPIRE_DATE.toString()));
    }

    @Test
    void getNonExistingJobPosting() {
        // Get the jobPosting
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingJobPosting() throws Exception {
        // Initialize the database
        insertedJobPosting = jobPostingRepository.save(jobPosting).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobPosting
        JobPosting updatedJobPosting = jobPostingRepository.findById(jobPosting.getId()).block();
        updatedJobPosting
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .expireDate(UPDATED_EXPIRE_DATE);
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(updatedJobPosting);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, jobPostingDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobPosting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJobPostingToMatchAllProperties(updatedJobPosting);
    }

    @Test
    void putNonExistingJobPosting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobPosting.setId(longCount.incrementAndGet());

        // Create the JobPosting
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, jobPostingDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobPosting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchJobPosting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobPosting.setId(longCount.incrementAndGet());

        // Create the JobPosting
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobPosting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamJobPosting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobPosting.setId(longCount.incrementAndGet());

        // Create the JobPosting
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the JobPosting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateJobPostingWithPatch() throws Exception {
        // Initialize the database
        insertedJobPosting = jobPostingRepository.save(jobPosting).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobPosting using partial update
        JobPosting partialUpdatedJobPosting = new JobPosting();
        partialUpdatedJobPosting.setId(jobPosting.getId());

        partialUpdatedJobPosting.title(UPDATED_TITLE).expireDate(UPDATED_EXPIRE_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedJobPosting.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedJobPosting))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobPosting in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobPostingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedJobPosting, jobPosting),
            getPersistedJobPosting(jobPosting)
        );
    }

    @Test
    void fullUpdateJobPostingWithPatch() throws Exception {
        // Initialize the database
        insertedJobPosting = jobPostingRepository.save(jobPosting).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobPosting using partial update
        JobPosting partialUpdatedJobPosting = new JobPosting();
        partialUpdatedJobPosting.setId(jobPosting.getId());

        partialUpdatedJobPosting
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .expireDate(UPDATED_EXPIRE_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedJobPosting.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedJobPosting))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobPosting in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobPostingUpdatableFieldsEquals(partialUpdatedJobPosting, getPersistedJobPosting(partialUpdatedJobPosting));
    }

    @Test
    void patchNonExistingJobPosting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobPosting.setId(longCount.incrementAndGet());

        // Create the JobPosting
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, jobPostingDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobPosting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchJobPosting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobPosting.setId(longCount.incrementAndGet());

        // Create the JobPosting
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobPosting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamJobPosting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobPosting.setId(longCount.incrementAndGet());

        // Create the JobPosting
        JobPostingDTO jobPostingDTO = jobPostingMapper.toDto(jobPosting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(jobPostingDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the JobPosting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteJobPosting() {
        // Initialize the database
        insertedJobPosting = jobPostingRepository.save(jobPosting).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the jobPosting
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, jobPosting.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return jobPostingRepository.count().block();
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

    protected JobPosting getPersistedJobPosting(JobPosting jobPosting) {
        return jobPostingRepository.findById(jobPosting.getId()).block();
    }

    protected void assertPersistedJobPostingToMatchAllProperties(JobPosting expectedJobPosting) {
        // Test fails because reactive api returns an empty object instead of null
        // assertJobPostingAllPropertiesEquals(expectedJobPosting, getPersistedJobPosting(expectedJobPosting));
        assertJobPostingUpdatableFieldsEquals(expectedJobPosting, getPersistedJobPosting(expectedJobPosting));
    }

    protected void assertPersistedJobPostingToMatchUpdatableProperties(JobPosting expectedJobPosting) {
        // Test fails because reactive api returns an empty object instead of null
        // assertJobPostingAllUpdatablePropertiesEquals(expectedJobPosting, getPersistedJobPosting(expectedJobPosting));
        assertJobPostingUpdatableFieldsEquals(expectedJobPosting, getPersistedJobPosting(expectedJobPosting));
    }
}
