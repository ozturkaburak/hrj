package com.ab.hr.web.rest;

import static com.ab.hr.domain.UserLanguageAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.UserLanguage;
import com.ab.hr.domain.enumeration.LanguageLevel;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.UserLanguageRepository;
import com.ab.hr.repository.UserRepository;
import com.ab.hr.service.dto.UserLanguageDTO;
import com.ab.hr.service.mapper.UserLanguageMapper;
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
 * Integration tests for the {@link UserLanguageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserLanguageResourceIT {

    private static final LanguageLevel DEFAULT_LEVEL = LanguageLevel.BEGINNER;
    private static final LanguageLevel UPDATED_LEVEL = LanguageLevel.INTERMEDIATE;

    private static final Boolean DEFAULT_NATIVE_LANGUAGE = false;
    private static final Boolean UPDATED_NATIVE_LANGUAGE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-languages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserLanguageRepository userLanguageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLanguageMapper userLanguageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserLanguage userLanguage;

    private UserLanguage insertedUserLanguage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLanguage createEntity(EntityManager em) {
        UserLanguage userLanguage = new UserLanguage()
            .level(DEFAULT_LEVEL)
            .nativeLanguage(DEFAULT_NATIVE_LANGUAGE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return userLanguage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLanguage createUpdatedEntity(EntityManager em) {
        UserLanguage userLanguage = new UserLanguage()
            .level(UPDATED_LEVEL)
            .nativeLanguage(UPDATED_NATIVE_LANGUAGE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return userLanguage;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserLanguage.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        userLanguage = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserLanguage != null) {
            userLanguageRepository.delete(insertedUserLanguage).block();
            insertedUserLanguage = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createUserLanguage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserLanguage
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(userLanguage);
        var returnedUserLanguageDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(UserLanguageDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the UserLanguage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserLanguage = userLanguageMapper.toEntity(returnedUserLanguageDTO);
        assertUserLanguageUpdatableFieldsEquals(returnedUserLanguage, getPersistedUserLanguage(returnedUserLanguage));

        insertedUserLanguage = returnedUserLanguage;
    }

    @Test
    void createUserLanguageWithExistingId() throws Exception {
        // Create the UserLanguage with an existing ID
        userLanguage.setId(1L);
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(userLanguage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserLanguage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userLanguage.setLevel(null);

        // Create the UserLanguage, which fails.
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(userLanguage);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userLanguage.setCreatedAt(null);

        // Create the UserLanguage, which fails.
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(userLanguage);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllUserLanguages() {
        // Initialize the database
        insertedUserLanguage = userLanguageRepository.save(userLanguage).block();

        // Get all the userLanguageList
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
            .value(hasItem(userLanguage.getId().intValue()))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL.toString()))
            .jsonPath("$.[*].nativeLanguage")
            .value(hasItem(DEFAULT_NATIVE_LANGUAGE.booleanValue()))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].deletedAt")
            .value(hasItem(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getUserLanguage() {
        // Initialize the database
        insertedUserLanguage = userLanguageRepository.save(userLanguage).block();

        // Get the userLanguage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userLanguage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userLanguage.getId().intValue()))
            .jsonPath("$.level")
            .value(is(DEFAULT_LEVEL.toString()))
            .jsonPath("$.nativeLanguage")
            .value(is(DEFAULT_NATIVE_LANGUAGE.booleanValue()))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.deletedAt")
            .value(is(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getNonExistingUserLanguage() {
        // Get the userLanguage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserLanguage() throws Exception {
        // Initialize the database
        insertedUserLanguage = userLanguageRepository.save(userLanguage).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userLanguage
        UserLanguage updatedUserLanguage = userLanguageRepository.findById(userLanguage.getId()).block();
        updatedUserLanguage
            .level(UPDATED_LEVEL)
            .nativeLanguage(UPDATED_NATIVE_LANGUAGE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(updatedUserLanguage);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userLanguageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserLanguage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserLanguageToMatchAllProperties(updatedUserLanguage);
    }

    @Test
    void putNonExistingUserLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLanguage.setId(longCount.incrementAndGet());

        // Create the UserLanguage
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(userLanguage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userLanguageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserLanguage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLanguage.setId(longCount.incrementAndGet());

        // Create the UserLanguage
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(userLanguage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserLanguage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLanguage.setId(longCount.incrementAndGet());

        // Create the UserLanguage
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(userLanguage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserLanguage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserLanguageWithPatch() throws Exception {
        // Initialize the database
        insertedUserLanguage = userLanguageRepository.save(userLanguage).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userLanguage using partial update
        UserLanguage partialUpdatedUserLanguage = new UserLanguage();
        partialUpdatedUserLanguage.setId(userLanguage.getId());

        partialUpdatedUserLanguage
            .level(UPDATED_LEVEL)
            .nativeLanguage(UPDATED_NATIVE_LANGUAGE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserLanguage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserLanguage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserLanguage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserLanguageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserLanguage, userLanguage),
            getPersistedUserLanguage(userLanguage)
        );
    }

    @Test
    void fullUpdateUserLanguageWithPatch() throws Exception {
        // Initialize the database
        insertedUserLanguage = userLanguageRepository.save(userLanguage).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userLanguage using partial update
        UserLanguage partialUpdatedUserLanguage = new UserLanguage();
        partialUpdatedUserLanguage.setId(userLanguage.getId());

        partialUpdatedUserLanguage
            .level(UPDATED_LEVEL)
            .nativeLanguage(UPDATED_NATIVE_LANGUAGE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserLanguage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserLanguage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserLanguage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserLanguageUpdatableFieldsEquals(partialUpdatedUserLanguage, getPersistedUserLanguage(partialUpdatedUserLanguage));
    }

    @Test
    void patchNonExistingUserLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLanguage.setId(longCount.incrementAndGet());

        // Create the UserLanguage
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(userLanguage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userLanguageDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserLanguage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLanguage.setId(longCount.incrementAndGet());

        // Create the UserLanguage
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(userLanguage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserLanguage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLanguage.setId(longCount.incrementAndGet());

        // Create the UserLanguage
        UserLanguageDTO userLanguageDTO = userLanguageMapper.toDto(userLanguage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userLanguageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserLanguage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserLanguage() {
        // Initialize the database
        insertedUserLanguage = userLanguageRepository.save(userLanguage).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userLanguage
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userLanguage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userLanguageRepository.count().block();
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

    protected UserLanguage getPersistedUserLanguage(UserLanguage userLanguage) {
        return userLanguageRepository.findById(userLanguage.getId()).block();
    }

    protected void assertPersistedUserLanguageToMatchAllProperties(UserLanguage expectedUserLanguage) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserLanguageAllPropertiesEquals(expectedUserLanguage, getPersistedUserLanguage(expectedUserLanguage));
        assertUserLanguageUpdatableFieldsEquals(expectedUserLanguage, getPersistedUserLanguage(expectedUserLanguage));
    }

    protected void assertPersistedUserLanguageToMatchUpdatableProperties(UserLanguage expectedUserLanguage) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserLanguageAllUpdatablePropertiesEquals(expectedUserLanguage, getPersistedUserLanguage(expectedUserLanguage));
        assertUserLanguageUpdatableFieldsEquals(expectedUserLanguage, getPersistedUserLanguage(expectedUserLanguage));
    }
}
