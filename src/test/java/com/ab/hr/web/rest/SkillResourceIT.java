package com.ab.hr.web.rest;

import static com.ab.hr.domain.SkillAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.Skill;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.SkillRepository;
import com.ab.hr.service.dto.SkillDTO;
import com.ab.hr.service.mapper.SkillMapper;
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
 * Integration tests for the {@link SkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SkillResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Skill skill;

    private Skill insertedSkill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createEntity(EntityManager em) {
        Skill skill = new Skill()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return skill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createUpdatedEntity(EntityManager em) {
        Skill skill = new Skill()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return skill;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Skill.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        skill = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSkill != null) {
            skillRepository.delete(insertedSkill).block();
            insertedSkill = null;
        }
        deleteEntities(em);
    }

    @Test
    void createSkill() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);
        var returnedSkillDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SkillDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Skill in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSkill = skillMapper.toEntity(returnedSkillDTO);
        assertSkillUpdatableFieldsEquals(returnedSkill, getPersistedSkill(returnedSkill));

        insertedSkill = returnedSkill;
    }

    @Test
    void createSkillWithExistingId() throws Exception {
        // Create the Skill with an existing ID
        skill.setId(1L);
        SkillDTO skillDTO = skillMapper.toDto(skill);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skill.setName(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skill.setCreatedAt(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSkills() {
        // Initialize the database
        insertedSkill = skillRepository.save(skill).block();

        // Get all the skillList
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
            .value(hasItem(skill.getId().intValue()))
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
    void getSkill() {
        // Initialize the database
        insertedSkill = skillRepository.save(skill).block();

        // Get the skill
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, skill.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(skill.getId().intValue()))
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
    void getNonExistingSkill() {
        // Get the skill
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSkill() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.save(skill).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skill
        Skill updatedSkill = skillRepository.findById(skill.getId()).block();
        updatedSkill.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).deletedAt(UPDATED_DELETED_AT);
        SkillDTO skillDTO = skillMapper.toDto(updatedSkill);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, skillDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSkillToMatchAllProperties(updatedSkill);
    }

    @Test
    void putNonExistingSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, skillDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSkillWithPatch() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.save(skill).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skill using partial update
        Skill partialUpdatedSkill = new Skill();
        partialUpdatedSkill.setId(skill.getId());

        partialUpdatedSkill.createdAt(UPDATED_CREATED_AT).deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSkill.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSkill))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Skill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSkill, skill), getPersistedSkill(skill));
    }

    @Test
    void fullUpdateSkillWithPatch() throws Exception {
        // Initialize the database
        insertedSkill = skillRepository.save(skill).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skill using partial update
        Skill partialUpdatedSkill = new Skill();
        partialUpdatedSkill.setId(skill.getId());

        partialUpdatedSkill.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSkill.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSkill))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Skill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillUpdatableFieldsEquals(partialUpdatedSkill, getPersistedSkill(partialUpdatedSkill));
    }

    @Test
    void patchNonExistingSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, skillDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Skill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSkill() {
        // Initialize the database
        insertedSkill = skillRepository.save(skill).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the skill
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, skill.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return skillRepository.count().block();
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

    protected Skill getPersistedSkill(Skill skill) {
        return skillRepository.findById(skill.getId()).block();
    }

    protected void assertPersistedSkillToMatchAllProperties(Skill expectedSkill) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSkillAllPropertiesEquals(expectedSkill, getPersistedSkill(expectedSkill));
        assertSkillUpdatableFieldsEquals(expectedSkill, getPersistedSkill(expectedSkill));
    }

    protected void assertPersistedSkillToMatchUpdatableProperties(Skill expectedSkill) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSkillAllUpdatablePropertiesEquals(expectedSkill, getPersistedSkill(expectedSkill));
        assertSkillUpdatableFieldsEquals(expectedSkill, getPersistedSkill(expectedSkill));
    }
}
