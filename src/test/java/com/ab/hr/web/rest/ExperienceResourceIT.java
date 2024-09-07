package com.ab.hr.web.rest;

import static com.ab.hr.domain.ExperienceAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.Experience;
import com.ab.hr.domain.enumeration.ContractType;
import com.ab.hr.domain.enumeration.WorkType;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.ExperienceRepository;
import com.ab.hr.service.dto.ExperienceDTO;
import com.ab.hr.service.mapper.ExperienceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ExperienceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ExperienceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final WorkType DEFAULT_WORK_TYPE = WorkType.HYBRID;
    private static final WorkType UPDATED_WORK_TYPE = WorkType.REMOTE;

    private static final ContractType DEFAULT_CONTRACT_TYPE = ContractType.CONTRACTOR;
    private static final ContractType UPDATED_CONTRACT_TYPE = ContractType.VOLUNTEER;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/experiences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private ExperienceMapper experienceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Experience experience;

    private Experience insertedExperience;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Experience createEntity(EntityManager em) {
        Experience experience = new Experience()
            .title(DEFAULT_TITLE)
            .workType(DEFAULT_WORK_TYPE)
            .contractType(DEFAULT_CONTRACT_TYPE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return experience;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Experience createUpdatedEntity(EntityManager em) {
        Experience experience = new Experience()
            .title(UPDATED_TITLE)
            .workType(UPDATED_WORK_TYPE)
            .contractType(UPDATED_CONTRACT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return experience;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Experience.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        experience = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedExperience != null) {
            experienceRepository.delete(insertedExperience).block();
            insertedExperience = null;
        }
        deleteEntities(em);
    }

    @Test
    void createExperience() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);
        var returnedExperienceDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ExperienceDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Experience in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExperience = experienceMapper.toEntity(returnedExperienceDTO);
        assertExperienceUpdatableFieldsEquals(returnedExperience, getPersistedExperience(returnedExperience));

        insertedExperience = returnedExperience;
    }

    @Test
    void createExperienceWithExistingId() throws Exception {
        // Create the Experience with an existing ID
        experience.setId(1L);
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Experience in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        experience.setTitle(null);

        // Create the Experience, which fails.
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkWorkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        experience.setWorkType(null);

        // Create the Experience, which fails.
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkContractTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        experience.setContractType(null);

        // Create the Experience, which fails.
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        experience.setCreatedAt(null);

        // Create the Experience, which fails.
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllExperiences() {
        // Initialize the database
        insertedExperience = experienceRepository.save(experience).block();

        // Get all the experienceList
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
            .value(hasItem(experience.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].workType")
            .value(hasItem(DEFAULT_WORK_TYPE.toString()))
            .jsonPath("$.[*].contractType")
            .value(hasItem(DEFAULT_CONTRACT_TYPE.toString()))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].deletedAt")
            .value(hasItem(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getExperience() {
        // Initialize the database
        insertedExperience = experienceRepository.save(experience).block();

        // Get the experience
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, experience.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(experience.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.workType")
            .value(is(DEFAULT_WORK_TYPE.toString()))
            .jsonPath("$.contractType")
            .value(is(DEFAULT_CONTRACT_TYPE.toString()))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.deletedAt")
            .value(is(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getNonExistingExperience() {
        // Get the experience
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingExperience() throws Exception {
        // Initialize the database
        insertedExperience = experienceRepository.save(experience).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the experience
        Experience updatedExperience = experienceRepository.findById(experience.getId()).block();
        updatedExperience
            .title(UPDATED_TITLE)
            .workType(UPDATED_WORK_TYPE)
            .contractType(UPDATED_CONTRACT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        ExperienceDTO experienceDTO = experienceMapper.toDto(updatedExperience);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, experienceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Experience in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExperienceToMatchAllProperties(updatedExperience);
    }

    @Test
    void putNonExistingExperience() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        experience.setId(longCount.incrementAndGet());

        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, experienceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Experience in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchExperience() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        experience.setId(longCount.incrementAndGet());

        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Experience in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamExperience() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        experience.setId(longCount.incrementAndGet());

        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Experience in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateExperienceWithPatch() throws Exception {
        // Initialize the database
        insertedExperience = experienceRepository.save(experience).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the experience using partial update
        Experience partialUpdatedExperience = new Experience();
        partialUpdatedExperience.setId(experience.getId());

        partialUpdatedExperience
            .title(UPDATED_TITLE)
            .contractType(UPDATED_CONTRACT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExperience.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedExperience))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Experience in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExperienceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExperience, experience),
            getPersistedExperience(experience)
        );
    }

    @Test
    void fullUpdateExperienceWithPatch() throws Exception {
        // Initialize the database
        insertedExperience = experienceRepository.save(experience).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the experience using partial update
        Experience partialUpdatedExperience = new Experience();
        partialUpdatedExperience.setId(experience.getId());

        partialUpdatedExperience
            .title(UPDATED_TITLE)
            .workType(UPDATED_WORK_TYPE)
            .contractType(UPDATED_CONTRACT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExperience.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedExperience))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Experience in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExperienceUpdatableFieldsEquals(partialUpdatedExperience, getPersistedExperience(partialUpdatedExperience));
    }

    @Test
    void patchNonExistingExperience() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        experience.setId(longCount.incrementAndGet());

        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, experienceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Experience in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchExperience() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        experience.setId(longCount.incrementAndGet());

        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Experience in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamExperience() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        experience.setId(longCount.incrementAndGet());

        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(experienceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Experience in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteExperience() {
        // Initialize the database
        insertedExperience = experienceRepository.save(experience).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the experience
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, experience.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return experienceRepository.count().block();
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

    protected Experience getPersistedExperience(Experience experience) {
        return experienceRepository.findById(experience.getId()).block();
    }

    protected void assertPersistedExperienceToMatchAllProperties(Experience expectedExperience) {
        // Test fails because reactive api returns an empty object instead of null
        // assertExperienceAllPropertiesEquals(expectedExperience, getPersistedExperience(expectedExperience));
        assertExperienceUpdatableFieldsEquals(expectedExperience, getPersistedExperience(expectedExperience));
    }

    protected void assertPersistedExperienceToMatchUpdatableProperties(Experience expectedExperience) {
        // Test fails because reactive api returns an empty object instead of null
        // assertExperienceAllUpdatablePropertiesEquals(expectedExperience, getPersistedExperience(expectedExperience));
        assertExperienceUpdatableFieldsEquals(expectedExperience, getPersistedExperience(expectedExperience));
    }
}
