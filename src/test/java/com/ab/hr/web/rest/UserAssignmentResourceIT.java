package com.ab.hr.web.rest;

import static com.ab.hr.domain.UserAssignmentAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.UserAssignment;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.UserAssignmentRepository;
import com.ab.hr.repository.UserRepository;
import com.ab.hr.service.dto.UserAssignmentDTO;
import com.ab.hr.service.mapper.UserAssignmentMapper;
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
 * Integration tests for the {@link UserAssignmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserAssignmentResourceIT {

    private static final Instant DEFAULT_ASSIGNED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ASSIGNED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_JOINED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_JOINED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FINISHED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISHED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAssignmentRepository userAssignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAssignmentMapper userAssignmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserAssignment userAssignment;

    private UserAssignment insertedUserAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAssignment createEntity(EntityManager em) {
        UserAssignment userAssignment = new UserAssignment()
            .assignedAt(DEFAULT_ASSIGNED_AT)
            .joinedAt(DEFAULT_JOINED_AT)
            .finishedAt(DEFAULT_FINISHED_AT);
        return userAssignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAssignment createUpdatedEntity(EntityManager em) {
        UserAssignment userAssignment = new UserAssignment()
            .assignedAt(UPDATED_ASSIGNED_AT)
            .joinedAt(UPDATED_JOINED_AT)
            .finishedAt(UPDATED_FINISHED_AT);
        return userAssignment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserAssignment.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        userAssignment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserAssignment != null) {
            userAssignmentRepository.delete(insertedUserAssignment).block();
            insertedUserAssignment = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createUserAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAssignment
        UserAssignmentDTO userAssignmentDTO = userAssignmentMapper.toDto(userAssignment);
        var returnedUserAssignmentDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAssignmentDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(UserAssignmentDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the UserAssignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserAssignment = userAssignmentMapper.toEntity(returnedUserAssignmentDTO);
        assertUserAssignmentUpdatableFieldsEquals(returnedUserAssignment, getPersistedUserAssignment(returnedUserAssignment));

        insertedUserAssignment = returnedUserAssignment;
    }

    @Test
    void createUserAssignmentWithExistingId() throws Exception {
        // Create the UserAssignment with an existing ID
        userAssignment.setId(1L);
        UserAssignmentDTO userAssignmentDTO = userAssignmentMapper.toDto(userAssignment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkAssignedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAssignment.setAssignedAt(null);

        // Create the UserAssignment, which fails.
        UserAssignmentDTO userAssignmentDTO = userAssignmentMapper.toDto(userAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllUserAssignments() {
        // Initialize the database
        insertedUserAssignment = userAssignmentRepository.save(userAssignment).block();

        // Get all the userAssignmentList
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
            .value(hasItem(userAssignment.getId().intValue()))
            .jsonPath("$.[*].assignedAt")
            .value(hasItem(DEFAULT_ASSIGNED_AT.toString()))
            .jsonPath("$.[*].joinedAt")
            .value(hasItem(DEFAULT_JOINED_AT.toString()))
            .jsonPath("$.[*].finishedAt")
            .value(hasItem(DEFAULT_FINISHED_AT.toString()));
    }

    @Test
    void getUserAssignment() {
        // Initialize the database
        insertedUserAssignment = userAssignmentRepository.save(userAssignment).block();

        // Get the userAssignment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userAssignment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userAssignment.getId().intValue()))
            .jsonPath("$.assignedAt")
            .value(is(DEFAULT_ASSIGNED_AT.toString()))
            .jsonPath("$.joinedAt")
            .value(is(DEFAULT_JOINED_AT.toString()))
            .jsonPath("$.finishedAt")
            .value(is(DEFAULT_FINISHED_AT.toString()));
    }

    @Test
    void getNonExistingUserAssignment() {
        // Get the userAssignment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserAssignment() throws Exception {
        // Initialize the database
        insertedUserAssignment = userAssignmentRepository.save(userAssignment).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAssignment
        UserAssignment updatedUserAssignment = userAssignmentRepository.findById(userAssignment.getId()).block();
        updatedUserAssignment.assignedAt(UPDATED_ASSIGNED_AT).joinedAt(UPDATED_JOINED_AT).finishedAt(UPDATED_FINISHED_AT);
        UserAssignmentDTO userAssignmentDTO = userAssignmentMapper.toDto(updatedUserAssignment);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userAssignmentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAssignmentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAssignmentToMatchAllProperties(updatedUserAssignment);
    }

    @Test
    void putNonExistingUserAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssignment.setId(longCount.incrementAndGet());

        // Create the UserAssignment
        UserAssignmentDTO userAssignmentDTO = userAssignmentMapper.toDto(userAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userAssignmentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssignment.setId(longCount.incrementAndGet());

        // Create the UserAssignment
        UserAssignmentDTO userAssignmentDTO = userAssignmentMapper.toDto(userAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssignment.setId(longCount.incrementAndGet());

        // Create the UserAssignment
        UserAssignmentDTO userAssignmentDTO = userAssignmentMapper.toDto(userAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userAssignmentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedUserAssignment = userAssignmentRepository.save(userAssignment).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAssignment using partial update
        UserAssignment partialUpdatedUserAssignment = new UserAssignment();
        partialUpdatedUserAssignment.setId(userAssignment.getId());

        partialUpdatedUserAssignment.assignedAt(UPDATED_ASSIGNED_AT).joinedAt(UPDATED_JOINED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserAssignment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserAssignment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAssignment, userAssignment),
            getPersistedUserAssignment(userAssignment)
        );
    }

    @Test
    void fullUpdateUserAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedUserAssignment = userAssignmentRepository.save(userAssignment).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAssignment using partial update
        UserAssignment partialUpdatedUserAssignment = new UserAssignment();
        partialUpdatedUserAssignment.setId(userAssignment.getId());

        partialUpdatedUserAssignment.assignedAt(UPDATED_ASSIGNED_AT).joinedAt(UPDATED_JOINED_AT).finishedAt(UPDATED_FINISHED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserAssignment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserAssignment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAssignmentUpdatableFieldsEquals(partialUpdatedUserAssignment, getPersistedUserAssignment(partialUpdatedUserAssignment));
    }

    @Test
    void patchNonExistingUserAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssignment.setId(longCount.incrementAndGet());

        // Create the UserAssignment
        UserAssignmentDTO userAssignmentDTO = userAssignmentMapper.toDto(userAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userAssignmentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssignment.setId(longCount.incrementAndGet());

        // Create the UserAssignment
        UserAssignmentDTO userAssignmentDTO = userAssignmentMapper.toDto(userAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssignment.setId(longCount.incrementAndGet());

        // Create the UserAssignment
        UserAssignmentDTO userAssignmentDTO = userAssignmentMapper.toDto(userAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userAssignmentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserAssignment() {
        // Initialize the database
        insertedUserAssignment = userAssignmentRepository.save(userAssignment).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAssignment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userAssignment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAssignmentRepository.count().block();
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

    protected UserAssignment getPersistedUserAssignment(UserAssignment userAssignment) {
        return userAssignmentRepository.findById(userAssignment.getId()).block();
    }

    protected void assertPersistedUserAssignmentToMatchAllProperties(UserAssignment expectedUserAssignment) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserAssignmentAllPropertiesEquals(expectedUserAssignment, getPersistedUserAssignment(expectedUserAssignment));
        assertUserAssignmentUpdatableFieldsEquals(expectedUserAssignment, getPersistedUserAssignment(expectedUserAssignment));
    }

    protected void assertPersistedUserAssignmentToMatchUpdatableProperties(UserAssignment expectedUserAssignment) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserAssignmentAllUpdatablePropertiesEquals(expectedUserAssignment, getPersistedUserAssignment(expectedUserAssignment));
        assertUserAssignmentUpdatableFieldsEquals(expectedUserAssignment, getPersistedUserAssignment(expectedUserAssignment));
    }
}
