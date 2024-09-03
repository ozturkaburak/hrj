package com.ab.hr.web.rest;

import static com.ab.hr.domain.CertificateAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.Certificate;
import com.ab.hr.repository.CertificateRepository;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.service.dto.CertificateDTO;
import com.ab.hr.service.mapper.CertificateMapper;
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
 * Integration tests for the {@link CertificateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CertificateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/certificates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CertificateMapper certificateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Certificate certificate;

    private Certificate insertedCertificate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificate createEntity(EntityManager em) {
        Certificate certificate = new Certificate()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return certificate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificate createUpdatedEntity(EntityManager em) {
        Certificate certificate = new Certificate()
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return certificate;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Certificate.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        certificate = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCertificate != null) {
            certificateRepository.delete(insertedCertificate).block();
            insertedCertificate = null;
        }
        deleteEntities(em);
    }

    @Test
    void createCertificate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);
        var returnedCertificateDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CertificateDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Certificate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCertificate = certificateMapper.toEntity(returnedCertificateDTO);
        assertCertificateUpdatableFieldsEquals(returnedCertificate, getPersistedCertificate(returnedCertificate));

        insertedCertificate = returnedCertificate;
    }

    @Test
    void createCertificateWithExistingId() throws Exception {
        // Create the Certificate with an existing ID
        certificate.setId(1L);
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Certificate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        certificate.setName(null);

        // Create the Certificate, which fails.
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        certificate.setCreatedAt(null);

        // Create the Certificate, which fails.
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCertificates() {
        // Initialize the database
        insertedCertificate = certificateRepository.save(certificate).block();

        // Get all the certificateList
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
            .value(hasItem(certificate.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
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
    void getCertificate() {
        // Initialize the database
        insertedCertificate = certificateRepository.save(certificate).block();

        // Get the certificate
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, certificate.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(certificate.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
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
    void getNonExistingCertificate() {
        // Get the certificate
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCertificate() throws Exception {
        // Initialize the database
        insertedCertificate = certificateRepository.save(certificate).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the certificate
        Certificate updatedCertificate = certificateRepository.findById(certificate.getId()).block();
        updatedCertificate
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        CertificateDTO certificateDTO = certificateMapper.toDto(updatedCertificate);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, certificateDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Certificate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCertificateToMatchAllProperties(updatedCertificate);
    }

    @Test
    void putNonExistingCertificate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        certificate.setId(longCount.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, certificateDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Certificate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCertificate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        certificate.setId(longCount.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Certificate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCertificate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        certificate.setId(longCount.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Certificate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCertificateWithPatch() throws Exception {
        // Initialize the database
        insertedCertificate = certificateRepository.save(certificate).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the certificate using partial update
        Certificate partialUpdatedCertificate = new Certificate();
        partialUpdatedCertificate.setId(certificate.getId());

        partialUpdatedCertificate
            .name(UPDATED_NAME)
            .endDate(UPDATED_END_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCertificate.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCertificate))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Certificate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCertificateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCertificate, certificate),
            getPersistedCertificate(certificate)
        );
    }

    @Test
    void fullUpdateCertificateWithPatch() throws Exception {
        // Initialize the database
        insertedCertificate = certificateRepository.save(certificate).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the certificate using partial update
        Certificate partialUpdatedCertificate = new Certificate();
        partialUpdatedCertificate.setId(certificate.getId());

        partialUpdatedCertificate
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCertificate.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCertificate))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Certificate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCertificateUpdatableFieldsEquals(partialUpdatedCertificate, getPersistedCertificate(partialUpdatedCertificate));
    }

    @Test
    void patchNonExistingCertificate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        certificate.setId(longCount.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, certificateDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Certificate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCertificate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        certificate.setId(longCount.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Certificate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCertificate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        certificate.setId(longCount.incrementAndGet());

        // Create the Certificate
        CertificateDTO certificateDTO = certificateMapper.toDto(certificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(certificateDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Certificate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCertificate() {
        // Initialize the database
        insertedCertificate = certificateRepository.save(certificate).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the certificate
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, certificate.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return certificateRepository.count().block();
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

    protected Certificate getPersistedCertificate(Certificate certificate) {
        return certificateRepository.findById(certificate.getId()).block();
    }

    protected void assertPersistedCertificateToMatchAllProperties(Certificate expectedCertificate) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCertificateAllPropertiesEquals(expectedCertificate, getPersistedCertificate(expectedCertificate));
        assertCertificateUpdatableFieldsEquals(expectedCertificate, getPersistedCertificate(expectedCertificate));
    }

    protected void assertPersistedCertificateToMatchUpdatableProperties(Certificate expectedCertificate) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCertificateAllUpdatablePropertiesEquals(expectedCertificate, getPersistedCertificate(expectedCertificate));
        assertCertificateUpdatableFieldsEquals(expectedCertificate, getPersistedCertificate(expectedCertificate));
    }
}
