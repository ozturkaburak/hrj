package com.ab.hr.web.rest;

import static com.ab.hr.domain.UserSkillAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.UserSkill;
import com.ab.hr.domain.enumeration.SkillLevel;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.UserRepository;
import com.ab.hr.repository.UserSkillRepository;
import com.ab.hr.service.dto.UserSkillDTO;
import com.ab.hr.service.mapper.UserSkillMapper;
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
 * Integration tests for the {@link UserSkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserSkillResourceIT {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final SkillLevel DEFAULT_LEVEL = SkillLevel.EXPERT;
    private static final SkillLevel UPDATED_LEVEL = SkillLevel.BEGINNER;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSkillMapper userSkillMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserSkill userSkill;

    private UserSkill insertedUserSkill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSkill createEntity(EntityManager em) {
        UserSkill userSkill = new UserSkill()
            .year(DEFAULT_YEAR)
            .level(DEFAULT_LEVEL)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return userSkill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSkill createUpdatedEntity(EntityManager em) {
        UserSkill userSkill = new UserSkill()
            .year(UPDATED_YEAR)
            .level(UPDATED_LEVEL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return userSkill;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserSkill.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        userSkill = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserSkill != null) {
            userSkillRepository.delete(insertedUserSkill).block();
            insertedUserSkill = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createUserSkill() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);
        var returnedUserSkillDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(UserSkillDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the UserSkill in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserSkill = userSkillMapper.toEntity(returnedUserSkillDTO);
        assertUserSkillUpdatableFieldsEquals(returnedUserSkill, getPersistedUserSkill(returnedUserSkill));

        insertedUserSkill = returnedUserSkill;
    }

    @Test
    void createUserSkillWithExistingId() throws Exception {
        // Create the UserSkill with an existing ID
        userSkill.setId(1L);
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSkill.setLevel(null);

        // Create the UserSkill, which fails.
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSkill.setCreatedAt(null);

        // Create the UserSkill, which fails.
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllUserSkills() {
        // Initialize the database
        insertedUserSkill = userSkillRepository.save(userSkill).block();

        // Get all the userSkillList
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
            .value(hasItem(userSkill.getId().intValue()))
            .jsonPath("$.[*].year")
            .value(hasItem(DEFAULT_YEAR))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL.toString()))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].deletedAt")
            .value(hasItem(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getUserSkill() {
        // Initialize the database
        insertedUserSkill = userSkillRepository.save(userSkill).block();

        // Get the userSkill
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userSkill.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userSkill.getId().intValue()))
            .jsonPath("$.year")
            .value(is(DEFAULT_YEAR))
            .jsonPath("$.level")
            .value(is(DEFAULT_LEVEL.toString()))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.deletedAt")
            .value(is(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getNonExistingUserSkill() {
        // Get the userSkill
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserSkill() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.save(userSkill).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSkill
        UserSkill updatedUserSkill = userSkillRepository.findById(userSkill.getId()).block();
        updatedUserSkill
            .year(UPDATED_YEAR)
            .level(UPDATED_LEVEL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(updatedUserSkill);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userSkillDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserSkillToMatchAllProperties(updatedUserSkill);
    }

    @Test
    void putNonExistingUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userSkillDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserSkillWithPatch() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.save(userSkill).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSkill using partial update
        UserSkill partialUpdatedUserSkill = new UserSkill();
        partialUpdatedUserSkill.setId(userSkill.getId());

        partialUpdatedUserSkill.year(UPDATED_YEAR).level(UPDATED_LEVEL).deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserSkill.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserSkill))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserSkill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSkillUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserSkill, userSkill),
            getPersistedUserSkill(userSkill)
        );
    }

    @Test
    void fullUpdateUserSkillWithPatch() throws Exception {
        // Initialize the database
        insertedUserSkill = userSkillRepository.save(userSkill).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSkill using partial update
        UserSkill partialUpdatedUserSkill = new UserSkill();
        partialUpdatedUserSkill.setId(userSkill.getId());

        partialUpdatedUserSkill
            .year(UPDATED_YEAR)
            .level(UPDATED_LEVEL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserSkill.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserSkill))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserSkill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSkillUpdatableFieldsEquals(partialUpdatedUserSkill, getPersistedUserSkill(partialUpdatedUserSkill));
    }

    @Test
    void patchNonExistingUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userSkillDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSkill.setId(longCount.incrementAndGet());

        // Create the UserSkill
        UserSkillDTO userSkillDTO = userSkillMapper.toDto(userSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userSkillDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserSkill() {
        // Initialize the database
        insertedUserSkill = userSkillRepository.save(userSkill).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userSkill
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userSkill.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userSkillRepository.count().block();
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

    protected UserSkill getPersistedUserSkill(UserSkill userSkill) {
        return userSkillRepository.findById(userSkill.getId()).block();
    }

    protected void assertPersistedUserSkillToMatchAllProperties(UserSkill expectedUserSkill) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserSkillAllPropertiesEquals(expectedUserSkill, getPersistedUserSkill(expectedUserSkill));
        assertUserSkillUpdatableFieldsEquals(expectedUserSkill, getPersistedUserSkill(expectedUserSkill));
    }

    protected void assertPersistedUserSkillToMatchUpdatableProperties(UserSkill expectedUserSkill) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserSkillAllUpdatablePropertiesEquals(expectedUserSkill, getPersistedUserSkill(expectedUserSkill));
        assertUserSkillUpdatableFieldsEquals(expectedUserSkill, getPersistedUserSkill(expectedUserSkill));
    }
}
