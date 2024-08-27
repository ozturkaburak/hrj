package com.ab.hr.web.rest;

import static com.ab.hr.domain.ResumeAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.Resume;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.ResumeRepository;
import com.ab.hr.repository.UserRepository;
import com.ab.hr.service.dto.ResumeDTO;
import com.ab.hr.service.mapper.ResumeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link ResumeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ResumeResourceIT {

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPLOAD_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOAD_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/resumes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Resume resume;

    private Resume insertedResume;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resume createEntity(EntityManager em) {
        Resume resume = new Resume()
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .fileType(DEFAULT_FILE_TYPE)
            .uploadDate(DEFAULT_UPLOAD_DATE);
        return resume;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resume createUpdatedEntity(EntityManager em) {
        Resume resume = new Resume()
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);
        return resume;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Resume.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        resume = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedResume != null) {
            resumeRepository.delete(insertedResume).block();
            insertedResume = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createResume() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);
        var returnedResumeDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ResumeDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Resume in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResume = resumeMapper.toEntity(returnedResumeDTO);
        assertResumeUpdatableFieldsEquals(returnedResume, getPersistedResume(returnedResume));

        insertedResume = returnedResume;
    }

    @Test
    void createResumeWithExistingId() throws Exception {
        // Create the Resume with an existing ID
        resume.setId(1L);
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkFileTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resume.setFileType(null);

        // Create the Resume, which fails.
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUploadDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resume.setUploadDate(null);

        // Create the Resume, which fails.
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllResumes() {
        // Initialize the database
        insertedResume = resumeRepository.save(resume).block();

        // Get all the resumeList
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
            .value(hasItem(resume.getId().intValue()))
            .jsonPath("$.[*].fileContentType")
            .value(hasItem(DEFAULT_FILE_CONTENT_TYPE))
            .jsonPath("$.[*].file")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE)))
            .jsonPath("$.[*].fileType")
            .value(hasItem(DEFAULT_FILE_TYPE))
            .jsonPath("$.[*].uploadDate")
            .value(hasItem(DEFAULT_UPLOAD_DATE.toString()));
    }

    @Test
    void getResume() {
        // Initialize the database
        insertedResume = resumeRepository.save(resume).block();

        // Get the resume
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, resume.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(resume.getId().intValue()))
            .jsonPath("$.fileContentType")
            .value(is(DEFAULT_FILE_CONTENT_TYPE))
            .jsonPath("$.file")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_FILE)))
            .jsonPath("$.fileType")
            .value(is(DEFAULT_FILE_TYPE))
            .jsonPath("$.uploadDate")
            .value(is(DEFAULT_UPLOAD_DATE.toString()));
    }

    @Test
    void getNonExistingResume() {
        // Get the resume
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingResume() throws Exception {
        // Initialize the database
        insertedResume = resumeRepository.save(resume).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resume
        Resume updatedResume = resumeRepository.findById(resume.getId()).block();
        updatedResume
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);
        ResumeDTO resumeDTO = resumeMapper.toDto(updatedResume);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, resumeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResumeToMatchAllProperties(updatedResume);
    }

    @Test
    void putNonExistingResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, resumeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateResumeWithPatch() throws Exception {
        // Initialize the database
        insertedResume = resumeRepository.save(resume).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resume using partial update
        Resume partialUpdatedResume = new Resume();
        partialUpdatedResume.setId(resume.getId());

        partialUpdatedResume
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResume.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedResume))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Resume in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResumeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedResume, resume), getPersistedResume(resume));
    }

    @Test
    void fullUpdateResumeWithPatch() throws Exception {
        // Initialize the database
        insertedResume = resumeRepository.save(resume).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resume using partial update
        Resume partialUpdatedResume = new Resume();
        partialUpdatedResume.setId(resume.getId());

        partialUpdatedResume
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResume.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedResume))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Resume in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResumeUpdatableFieldsEquals(partialUpdatedResume, getPersistedResume(partialUpdatedResume));
    }

    @Test
    void patchNonExistingResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, resumeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(resumeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteResume() {
        // Initialize the database
        insertedResume = resumeRepository.save(resume).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the resume
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, resume.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return resumeRepository.count().block();
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

    protected Resume getPersistedResume(Resume resume) {
        return resumeRepository.findById(resume.getId()).block();
    }

    protected void assertPersistedResumeToMatchAllProperties(Resume expectedResume) {
        // Test fails because reactive api returns an empty object instead of null
        // assertResumeAllPropertiesEquals(expectedResume, getPersistedResume(expectedResume));
        assertResumeUpdatableFieldsEquals(expectedResume, getPersistedResume(expectedResume));
    }

    protected void assertPersistedResumeToMatchUpdatableProperties(Resume expectedResume) {
        // Test fails because reactive api returns an empty object instead of null
        // assertResumeAllUpdatablePropertiesEquals(expectedResume, getPersistedResume(expectedResume));
        assertResumeUpdatableFieldsEquals(expectedResume, getPersistedResume(expectedResume));
    }
}
