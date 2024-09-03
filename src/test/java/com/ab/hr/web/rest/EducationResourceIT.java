package com.ab.hr.web.rest;

import static com.ab.hr.domain.EducationAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.Education;
import com.ab.hr.repository.EducationRepository;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.service.dto.EducationDTO;
import com.ab.hr.service.mapper.EducationMapper;
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
 * Integration tests for the {@link EducationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EducationResourceIT {

    private static final String DEFAULT_SCHOOL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_DEGREE = "AAAAAAAAAA";
    private static final String UPDATED_DEGREE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVITIES = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITIES = "BBBBBBBBBB";

    private static final String DEFAULT_CLUBS = "AAAAAAAAAA";
    private static final String UPDATED_CLUBS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/educations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private EducationMapper educationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Education education;

    private Education insertedEducation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Education createEntity(EntityManager em) {
        Education education = new Education()
            .schoolName(DEFAULT_SCHOOL_NAME)
            .department(DEFAULT_DEPARTMENT)
            .degree(DEFAULT_DEGREE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .description(DEFAULT_DESCRIPTION)
            .activities(DEFAULT_ACTIVITIES)
            .clubs(DEFAULT_CLUBS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return education;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Education createUpdatedEntity(EntityManager em) {
        Education education = new Education()
            .schoolName(UPDATED_SCHOOL_NAME)
            .department(UPDATED_DEPARTMENT)
            .degree(UPDATED_DEGREE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .activities(UPDATED_ACTIVITIES)
            .clubs(UPDATED_CLUBS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return education;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Education.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        education = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEducation != null) {
            educationRepository.delete(insertedEducation).block();
            insertedEducation = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEducation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);
        var returnedEducationDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EducationDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Education in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEducation = educationMapper.toEntity(returnedEducationDTO);
        assertEducationUpdatableFieldsEquals(returnedEducation, getPersistedEducation(returnedEducation));

        insertedEducation = returnedEducation;
    }

    @Test
    void createEducationWithExistingId() throws Exception {
        // Create the Education with an existing ID
        education.setId(1L);
        EducationDTO educationDTO = educationMapper.toDto(education);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSchoolNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        education.setSchoolName(null);

        // Create the Education, which fails.
        EducationDTO educationDTO = educationMapper.toDto(education);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        education.setStartDate(null);

        // Create the Education, which fails.
        EducationDTO educationDTO = educationMapper.toDto(education);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        education.setCreatedAt(null);

        // Create the Education, which fails.
        EducationDTO educationDTO = educationMapper.toDto(education);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllEducations() {
        // Initialize the database
        insertedEducation = educationRepository.save(education).block();

        // Get all the educationList
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
            .value(hasItem(education.getId().intValue()))
            .jsonPath("$.[*].schoolName")
            .value(hasItem(DEFAULT_SCHOOL_NAME))
            .jsonPath("$.[*].department")
            .value(hasItem(DEFAULT_DEPARTMENT))
            .jsonPath("$.[*].degree")
            .value(hasItem(DEFAULT_DEGREE))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].activities")
            .value(hasItem(DEFAULT_ACTIVITIES))
            .jsonPath("$.[*].clubs")
            .value(hasItem(DEFAULT_CLUBS))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].deletedAt")
            .value(hasItem(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getEducation() {
        // Initialize the database
        insertedEducation = educationRepository.save(education).block();

        // Get the education
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, education.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(education.getId().intValue()))
            .jsonPath("$.schoolName")
            .value(is(DEFAULT_SCHOOL_NAME))
            .jsonPath("$.department")
            .value(is(DEFAULT_DEPARTMENT))
            .jsonPath("$.degree")
            .value(is(DEFAULT_DEGREE))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.activities")
            .value(is(DEFAULT_ACTIVITIES))
            .jsonPath("$.clubs")
            .value(is(DEFAULT_CLUBS))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.deletedAt")
            .value(is(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getNonExistingEducation() {
        // Get the education
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEducation() throws Exception {
        // Initialize the database
        insertedEducation = educationRepository.save(education).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the education
        Education updatedEducation = educationRepository.findById(education.getId()).block();
        updatedEducation
            .schoolName(UPDATED_SCHOOL_NAME)
            .department(UPDATED_DEPARTMENT)
            .degree(UPDATED_DEGREE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .activities(UPDATED_ACTIVITIES)
            .clubs(UPDATED_CLUBS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        EducationDTO educationDTO = educationMapper.toDto(updatedEducation);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, educationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEducationToMatchAllProperties(updatedEducation);
    }

    @Test
    void putNonExistingEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, educationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEducationWithPatch() throws Exception {
        // Initialize the database
        insertedEducation = educationRepository.save(education).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the education using partial update
        Education partialUpdatedEducation = new Education();
        partialUpdatedEducation.setId(education.getId());

        partialUpdatedEducation
            .schoolName(UPDATED_SCHOOL_NAME)
            .degree(UPDATED_DEGREE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .clubs(UPDATED_CLUBS)
            .deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEducation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEducation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Education in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEducationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEducation, education),
            getPersistedEducation(education)
        );
    }

    @Test
    void fullUpdateEducationWithPatch() throws Exception {
        // Initialize the database
        insertedEducation = educationRepository.save(education).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the education using partial update
        Education partialUpdatedEducation = new Education();
        partialUpdatedEducation.setId(education.getId());

        partialUpdatedEducation
            .schoolName(UPDATED_SCHOOL_NAME)
            .department(UPDATED_DEPARTMENT)
            .degree(UPDATED_DEGREE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .activities(UPDATED_ACTIVITIES)
            .clubs(UPDATED_CLUBS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEducation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEducation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Education in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEducationUpdatableFieldsEquals(partialUpdatedEducation, getPersistedEducation(partialUpdatedEducation));
    }

    @Test
    void patchNonExistingEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, educationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        education.setId(longCount.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(educationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Education in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEducation() {
        // Initialize the database
        insertedEducation = educationRepository.save(education).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the education
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, education.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return educationRepository.count().block();
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

    protected Education getPersistedEducation(Education education) {
        return educationRepository.findById(education.getId()).block();
    }

    protected void assertPersistedEducationToMatchAllProperties(Education expectedEducation) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEducationAllPropertiesEquals(expectedEducation, getPersistedEducation(expectedEducation));
        assertEducationUpdatableFieldsEquals(expectedEducation, getPersistedEducation(expectedEducation));
    }

    protected void assertPersistedEducationToMatchUpdatableProperties(Education expectedEducation) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEducationAllUpdatablePropertiesEquals(expectedEducation, getPersistedEducation(expectedEducation));
        assertEducationUpdatableFieldsEquals(expectedEducation, getPersistedEducation(expectedEducation));
    }
}
