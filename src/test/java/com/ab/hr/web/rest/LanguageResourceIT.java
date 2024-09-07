package com.ab.hr.web.rest;

import static com.ab.hr.domain.LanguageAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.Language;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.LanguageRepository;
import com.ab.hr.service.dto.LanguageDTO;
import com.ab.hr.service.mapper.LanguageMapper;
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
 * Integration tests for the {@link LanguageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LanguageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/languages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageMapper languageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Language language;

    private Language insertedLanguage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createEntity(EntityManager em) {
        Language language = new Language()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return language;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createUpdatedEntity(EntityManager em) {
        Language language = new Language()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return language;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Language.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        language = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLanguage != null) {
            languageRepository.delete(insertedLanguage).block();
            insertedLanguage = null;
        }
        deleteEntities(em);
    }

    @Test
    void createLanguage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);
        var returnedLanguageDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(LanguageDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Language in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLanguage = languageMapper.toEntity(returnedLanguageDTO);
        assertLanguageUpdatableFieldsEquals(returnedLanguage, getPersistedLanguage(returnedLanguage));

        insertedLanguage = returnedLanguage;
    }

    @Test
    void createLanguageWithExistingId() throws Exception {
        // Create the Language with an existing ID
        language.setId(1L);
        LanguageDTO languageDTO = languageMapper.toDto(language);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        language.setName(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.toDto(language);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        language.setCreatedAt(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.toDto(language);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllLanguages() {
        // Initialize the database
        insertedLanguage = languageRepository.save(language).block();

        // Get all the languageList
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
            .value(hasItem(language.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].deletedAt")
            .value(hasItem(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getLanguage() {
        // Initialize the database
        insertedLanguage = languageRepository.save(language).block();

        // Get the language
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, language.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(language.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.deletedAt")
            .value(is(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getNonExistingLanguage() {
        // Get the language
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLanguage() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.save(language).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the language
        Language updatedLanguage = languageRepository.findById(language.getId()).block();
        updatedLanguage.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).deletedAt(UPDATED_DELETED_AT);
        LanguageDTO languageDTO = languageMapper.toDto(updatedLanguage);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, languageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLanguageToMatchAllProperties(updatedLanguage);
    }

    @Test
    void putNonExistingLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, languageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLanguageWithPatch() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.save(language).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the language using partial update
        Language partialUpdatedLanguage = new Language();
        partialUpdatedLanguage.setId(language.getId());

        partialUpdatedLanguage.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLanguage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedLanguage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Language in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLanguageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLanguage, language), getPersistedLanguage(language));
    }

    @Test
    void fullUpdateLanguageWithPatch() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.save(language).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the language using partial update
        Language partialUpdatedLanguage = new Language();
        partialUpdatedLanguage.setId(language.getId());

        partialUpdatedLanguage.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLanguage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedLanguage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Language in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLanguageUpdatableFieldsEquals(partialUpdatedLanguage, getPersistedLanguage(partialUpdatedLanguage));
    }

    @Test
    void patchNonExistingLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, languageDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(languageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLanguage() {
        // Initialize the database
        insertedLanguage = languageRepository.save(language).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the language
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, language.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return languageRepository.count().block();
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

    protected Language getPersistedLanguage(Language language) {
        return languageRepository.findById(language.getId()).block();
    }

    protected void assertPersistedLanguageToMatchAllProperties(Language expectedLanguage) {
        // Test fails because reactive api returns an empty object instead of null
        // assertLanguageAllPropertiesEquals(expectedLanguage, getPersistedLanguage(expectedLanguage));
        assertLanguageUpdatableFieldsEquals(expectedLanguage, getPersistedLanguage(expectedLanguage));
    }

    protected void assertPersistedLanguageToMatchUpdatableProperties(Language expectedLanguage) {
        // Test fails because reactive api returns an empty object instead of null
        // assertLanguageAllUpdatablePropertiesEquals(expectedLanguage, getPersistedLanguage(expectedLanguage));
        assertLanguageUpdatableFieldsEquals(expectedLanguage, getPersistedLanguage(expectedLanguage));
    }
}
