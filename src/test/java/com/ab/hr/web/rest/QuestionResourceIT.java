package com.ab.hr.web.rest;

import static com.ab.hr.domain.QuestionAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.Question;
import com.ab.hr.domain.enumeration.QuestionType;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.QuestionRepository;
import com.ab.hr.service.dto.QuestionDTO;
import com.ab.hr.service.mapper.QuestionMapper;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class QuestionResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONS = "BBBBBBBBBB";

    private static final QuestionType DEFAULT_TYPE = QuestionType.SINGLE_CHOICE;
    private static final QuestionType UPDATED_TYPE = QuestionType.MULTIPLE_CHOICE;

    private static final String DEFAULT_CORRECT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_CORRECT_ANSWER = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Question question;

    private Question insertedQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .content(DEFAULT_CONTENT)
            .options(DEFAULT_OPTIONS)
            .type(DEFAULT_TYPE)
            .correctAnswer(DEFAULT_CORRECT_ANSWER)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return question;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question question = new Question()
            .content(UPDATED_CONTENT)
            .options(UPDATED_OPTIONS)
            .type(UPDATED_TYPE)
            .correctAnswer(UPDATED_CORRECT_ANSWER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return question;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Question.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        question = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedQuestion != null) {
            questionRepository.delete(insertedQuestion).block();
            insertedQuestion = null;
        }
        deleteEntities(em);
    }

    @Test
    void createQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);
        var returnedQuestionDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(QuestionDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Question in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestion = questionMapper.toEntity(returnedQuestionDTO);
        assertQuestionUpdatableFieldsEquals(returnedQuestion, getPersistedQuestion(returnedQuestion));

        insertedQuestion = returnedQuestion;
    }

    @Test
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setContent(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setType(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setCreatedAt(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllQuestions() {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        // Get all the questionList
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
            .value(hasItem(question.getId().intValue()))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].options")
            .value(hasItem(DEFAULT_OPTIONS))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].correctAnswer")
            .value(hasItem(DEFAULT_CORRECT_ANSWER))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].deletedAt")
            .value(hasItem(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getQuestion() {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        // Get the question
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, question.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(question.getId().intValue()))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT))
            .jsonPath("$.options")
            .value(is(DEFAULT_OPTIONS))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.correctAnswer")
            .value(is(DEFAULT_CORRECT_ANSWER))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.deletedAt")
            .value(is(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getNonExistingQuestion() {
        // Get the question
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).block();
        updatedQuestion
            .content(UPDATED_CONTENT)
            .options(UPDATED_OPTIONS)
            .type(UPDATED_TYPE)
            .correctAnswer(UPDATED_CORRECT_ANSWER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        QuestionDTO questionDTO = questionMapper.toDto(updatedQuestion);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, questionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionToMatchAllProperties(updatedQuestion);
    }

    @Test
    void putNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, questionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion.type(UPDATED_TYPE).deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuestion, question), getPersistedQuestion(question));
    }

    @Test
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .content(UPDATED_CONTENT)
            .options(UPDATED_OPTIONS)
            .type(UPDATED_TYPE)
            .correctAnswer(UPDATED_CORRECT_ANSWER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(partialUpdatedQuestion, getPersistedQuestion(partialUpdatedQuestion));
    }

    @Test
    void patchNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, questionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteQuestion() {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the question
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, question.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionRepository.count().block();
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

    protected Question getPersistedQuestion(Question question) {
        return questionRepository.findById(question.getId()).block();
    }

    protected void assertPersistedQuestionToMatchAllProperties(Question expectedQuestion) {
        // Test fails because reactive api returns an empty object instead of null
        // assertQuestionAllPropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
        assertQuestionUpdatableFieldsEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }

    protected void assertPersistedQuestionToMatchUpdatableProperties(Question expectedQuestion) {
        // Test fails because reactive api returns an empty object instead of null
        // assertQuestionAllUpdatablePropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
        assertQuestionUpdatableFieldsEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }
}
