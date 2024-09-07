package com.ab.hr.web.rest;

import static com.ab.hr.domain.AboutMeAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.AboutMe;
import com.ab.hr.domain.enumeration.SocialMediaType;
import com.ab.hr.repository.AboutMeRepository;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.service.dto.AboutMeDTO;
import com.ab.hr.service.mapper.AboutMeMapper;
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
 * Integration tests for the {@link AboutMeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AboutMeResourceIT {

    private static final SocialMediaType DEFAULT_SOCIAL_MEDIA = SocialMediaType.INSTAGRAM;
    private static final SocialMediaType UPDATED_SOCIAL_MEDIA = SocialMediaType.FACEBOOK;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/about-mes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AboutMeRepository aboutMeRepository;

    @Autowired
    private AboutMeMapper aboutMeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AboutMe aboutMe;

    private AboutMe insertedAboutMe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AboutMe createEntity(EntityManager em) {
        AboutMe aboutMe = new AboutMe()
            .socialMedia(DEFAULT_SOCIAL_MEDIA)
            .url(DEFAULT_URL)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return aboutMe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AboutMe createUpdatedEntity(EntityManager em) {
        AboutMe aboutMe = new AboutMe()
            .socialMedia(UPDATED_SOCIAL_MEDIA)
            .url(UPDATED_URL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return aboutMe;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AboutMe.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        aboutMe = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAboutMe != null) {
            aboutMeRepository.delete(insertedAboutMe).block();
            insertedAboutMe = null;
        }
        deleteEntities(em);
    }

    @Test
    void createAboutMe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AboutMe
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(aboutMe);
        var returnedAboutMeDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AboutMeDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the AboutMe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAboutMe = aboutMeMapper.toEntity(returnedAboutMeDTO);
        assertAboutMeUpdatableFieldsEquals(returnedAboutMe, getPersistedAboutMe(returnedAboutMe));

        insertedAboutMe = returnedAboutMe;
    }

    @Test
    void createAboutMeWithExistingId() throws Exception {
        // Create the AboutMe with an existing ID
        aboutMe.setId(1L);
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(aboutMe);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AboutMe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aboutMe.setUrl(null);

        // Create the AboutMe, which fails.
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(aboutMe);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aboutMe.setCreatedAt(null);

        // Create the AboutMe, which fails.
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(aboutMe);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAboutMes() {
        // Initialize the database
        insertedAboutMe = aboutMeRepository.save(aboutMe).block();

        // Get all the aboutMeList
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
            .value(hasItem(aboutMe.getId().intValue()))
            .jsonPath("$.[*].socialMedia")
            .value(hasItem(DEFAULT_SOCIAL_MEDIA.toString()))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].deletedAt")
            .value(hasItem(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getAboutMe() {
        // Initialize the database
        insertedAboutMe = aboutMeRepository.save(aboutMe).block();

        // Get the aboutMe
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, aboutMe.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(aboutMe.getId().intValue()))
            .jsonPath("$.socialMedia")
            .value(is(DEFAULT_SOCIAL_MEDIA.toString()))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.deletedAt")
            .value(is(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getNonExistingAboutMe() {
        // Get the aboutMe
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAboutMe() throws Exception {
        // Initialize the database
        insertedAboutMe = aboutMeRepository.save(aboutMe).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aboutMe
        AboutMe updatedAboutMe = aboutMeRepository.findById(aboutMe.getId()).block();
        updatedAboutMe
            .socialMedia(UPDATED_SOCIAL_MEDIA)
            .url(UPDATED_URL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(updatedAboutMe);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, aboutMeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AboutMe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAboutMeToMatchAllProperties(updatedAboutMe);
    }

    @Test
    void putNonExistingAboutMe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutMe.setId(longCount.incrementAndGet());

        // Create the AboutMe
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(aboutMe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, aboutMeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AboutMe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAboutMe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutMe.setId(longCount.incrementAndGet());

        // Create the AboutMe
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(aboutMe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AboutMe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAboutMe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutMe.setId(longCount.incrementAndGet());

        // Create the AboutMe
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(aboutMe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AboutMe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAboutMeWithPatch() throws Exception {
        // Initialize the database
        insertedAboutMe = aboutMeRepository.save(aboutMe).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aboutMe using partial update
        AboutMe partialUpdatedAboutMe = new AboutMe();
        partialUpdatedAboutMe.setId(aboutMe.getId());

        partialUpdatedAboutMe.createdAt(UPDATED_CREATED_AT).deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAboutMe.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAboutMe))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AboutMe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAboutMeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAboutMe, aboutMe), getPersistedAboutMe(aboutMe));
    }

    @Test
    void fullUpdateAboutMeWithPatch() throws Exception {
        // Initialize the database
        insertedAboutMe = aboutMeRepository.save(aboutMe).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aboutMe using partial update
        AboutMe partialUpdatedAboutMe = new AboutMe();
        partialUpdatedAboutMe.setId(aboutMe.getId());

        partialUpdatedAboutMe
            .socialMedia(UPDATED_SOCIAL_MEDIA)
            .url(UPDATED_URL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAboutMe.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAboutMe))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AboutMe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAboutMeUpdatableFieldsEquals(partialUpdatedAboutMe, getPersistedAboutMe(partialUpdatedAboutMe));
    }

    @Test
    void patchNonExistingAboutMe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutMe.setId(longCount.incrementAndGet());

        // Create the AboutMe
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(aboutMe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, aboutMeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AboutMe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAboutMe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutMe.setId(longCount.incrementAndGet());

        // Create the AboutMe
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(aboutMe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AboutMe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAboutMe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aboutMe.setId(longCount.incrementAndGet());

        // Create the AboutMe
        AboutMeDTO aboutMeDTO = aboutMeMapper.toDto(aboutMe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(aboutMeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AboutMe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAboutMe() {
        // Initialize the database
        insertedAboutMe = aboutMeRepository.save(aboutMe).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aboutMe
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, aboutMe.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aboutMeRepository.count().block();
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

    protected AboutMe getPersistedAboutMe(AboutMe aboutMe) {
        return aboutMeRepository.findById(aboutMe.getId()).block();
    }

    protected void assertPersistedAboutMeToMatchAllProperties(AboutMe expectedAboutMe) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAboutMeAllPropertiesEquals(expectedAboutMe, getPersistedAboutMe(expectedAboutMe));
        assertAboutMeUpdatableFieldsEquals(expectedAboutMe, getPersistedAboutMe(expectedAboutMe));
    }

    protected void assertPersistedAboutMeToMatchUpdatableProperties(AboutMe expectedAboutMe) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAboutMeAllUpdatablePropertiesEquals(expectedAboutMe, getPersistedAboutMe(expectedAboutMe));
        assertAboutMeUpdatableFieldsEquals(expectedAboutMe, getPersistedAboutMe(expectedAboutMe));
    }
}
