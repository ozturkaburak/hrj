package com.ab.hr.web.rest;

import static com.ab.hr.domain.UploadAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.Upload;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.UploadRepository;
import com.ab.hr.service.dto.UploadDTO;
import com.ab.hr.service.mapper.UploadMapper;
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
 * Integration tests for the {@link UploadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UploadResourceIT {

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPLOAD_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOAD_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/uploads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UploadRepository uploadRepository;

    @Autowired
    private UploadMapper uploadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Upload upload;

    private Upload insertedUpload;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upload createEntity(EntityManager em) {
        Upload upload = new Upload()
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .fileType(DEFAULT_FILE_TYPE)
            .uploadDate(DEFAULT_UPLOAD_DATE);
        return upload;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upload createUpdatedEntity(EntityManager em) {
        Upload upload = new Upload()
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);
        return upload;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Upload.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        upload = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUpload != null) {
            uploadRepository.delete(insertedUpload).block();
            insertedUpload = null;
        }
        deleteEntities(em);
    }

    @Test
    void createUpload() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Upload
        UploadDTO uploadDTO = uploadMapper.toDto(upload);
        var returnedUploadDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(UploadDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Upload in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUpload = uploadMapper.toEntity(returnedUploadDTO);
        assertUploadUpdatableFieldsEquals(returnedUpload, getPersistedUpload(returnedUpload));

        insertedUpload = returnedUpload;
    }

    @Test
    void createUploadWithExistingId() throws Exception {
        // Create the Upload with an existing ID
        upload.setId(1L);
        UploadDTO uploadDTO = uploadMapper.toDto(upload);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Upload in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkFileTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        upload.setFileType(null);

        // Create the Upload, which fails.
        UploadDTO uploadDTO = uploadMapper.toDto(upload);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUploadDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        upload.setUploadDate(null);

        // Create the Upload, which fails.
        UploadDTO uploadDTO = uploadMapper.toDto(upload);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllUploads() {
        // Initialize the database
        insertedUpload = uploadRepository.save(upload).block();

        // Get all the uploadList
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
            .value(hasItem(upload.getId().intValue()))
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
    void getUpload() {
        // Initialize the database
        insertedUpload = uploadRepository.save(upload).block();

        // Get the upload
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, upload.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(upload.getId().intValue()))
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
    void getNonExistingUpload() {
        // Get the upload
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUpload() throws Exception {
        // Initialize the database
        insertedUpload = uploadRepository.save(upload).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the upload
        Upload updatedUpload = uploadRepository.findById(upload.getId()).block();
        updatedUpload
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);
        UploadDTO uploadDTO = uploadMapper.toDto(updatedUpload);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, uploadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Upload in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUploadToMatchAllProperties(updatedUpload);
    }

    @Test
    void putNonExistingUpload() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upload.setId(longCount.incrementAndGet());

        // Create the Upload
        UploadDTO uploadDTO = uploadMapper.toDto(upload);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, uploadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Upload in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUpload() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upload.setId(longCount.incrementAndGet());

        // Create the Upload
        UploadDTO uploadDTO = uploadMapper.toDto(upload);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Upload in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUpload() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upload.setId(longCount.incrementAndGet());

        // Create the Upload
        UploadDTO uploadDTO = uploadMapper.toDto(upload);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Upload in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUploadWithPatch() throws Exception {
        // Initialize the database
        insertedUpload = uploadRepository.save(upload).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the upload using partial update
        Upload partialUpdatedUpload = new Upload();
        partialUpdatedUpload.setId(upload.getId());

        partialUpdatedUpload.file(UPDATED_FILE).fileContentType(UPDATED_FILE_CONTENT_TYPE).uploadDate(UPDATED_UPLOAD_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUpload.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUpload))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Upload in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUploadUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUpload, upload), getPersistedUpload(upload));
    }

    @Test
    void fullUpdateUploadWithPatch() throws Exception {
        // Initialize the database
        insertedUpload = uploadRepository.save(upload).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the upload using partial update
        Upload partialUpdatedUpload = new Upload();
        partialUpdatedUpload.setId(upload.getId());

        partialUpdatedUpload
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUpload.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUpload))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Upload in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUploadUpdatableFieldsEquals(partialUpdatedUpload, getPersistedUpload(partialUpdatedUpload));
    }

    @Test
    void patchNonExistingUpload() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upload.setId(longCount.incrementAndGet());

        // Create the Upload
        UploadDTO uploadDTO = uploadMapper.toDto(upload);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, uploadDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Upload in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUpload() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upload.setId(longCount.incrementAndGet());

        // Create the Upload
        UploadDTO uploadDTO = uploadMapper.toDto(upload);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Upload in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUpload() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upload.setId(longCount.incrementAndGet());

        // Create the Upload
        UploadDTO uploadDTO = uploadMapper.toDto(upload);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(uploadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Upload in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUpload() {
        // Initialize the database
        insertedUpload = uploadRepository.save(upload).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the upload
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, upload.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uploadRepository.count().block();
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

    protected Upload getPersistedUpload(Upload upload) {
        return uploadRepository.findById(upload.getId()).block();
    }

    protected void assertPersistedUploadToMatchAllProperties(Upload expectedUpload) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUploadAllPropertiesEquals(expectedUpload, getPersistedUpload(expectedUpload));
        assertUploadUpdatableFieldsEquals(expectedUpload, getPersistedUpload(expectedUpload));
    }

    protected void assertPersistedUploadToMatchUpdatableProperties(Upload expectedUpload) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUploadAllUpdatablePropertiesEquals(expectedUpload, getPersistedUpload(expectedUpload));
        assertUploadUpdatableFieldsEquals(expectedUpload, getPersistedUpload(expectedUpload));
    }
}
