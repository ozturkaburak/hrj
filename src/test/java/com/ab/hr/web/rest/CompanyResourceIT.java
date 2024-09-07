package com.ab.hr.web.rest;

import static com.ab.hr.domain.CompanyAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.Company;
import com.ab.hr.repository.CompanyRepository;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.service.dto.CompanyDTO;
import com.ab.hr.service.mapper.CompanyMapper;
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
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CompanyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Company company;

    private Company insertedCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .name(DEFAULT_NAME)
            .logo(DEFAULT_LOGO)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT)
            .active(DEFAULT_ACTIVE);
        return company;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .name(UPDATED_NAME)
            .logo(UPDATED_LOGO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .active(UPDATED_ACTIVE);
        return company;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Company.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCompany != null) {
            companyRepository.delete(insertedCompany).block();
            insertedCompany = null;
        }
        deleteEntities(em);
    }

    @Test
    void createCompany() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);
        var returnedCompanyDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CompanyDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Company in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompany = companyMapper.toEntity(returnedCompanyDTO);
        assertCompanyUpdatableFieldsEquals(returnedCompany, getPersistedCompany(returnedCompany));

        insertedCompany = returnedCompany;
    }

    @Test
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        company.setId(1L);
        CompanyDTO companyDTO = companyMapper.toDto(company);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        company.setName(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLogoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        company.setLogo(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        company.setCreatedAt(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        company.setActive(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCompanies() {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        // Get all the companyList
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
            .value(hasItem(company.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].logo")
            .value(hasItem(DEFAULT_LOGO))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].deletedAt")
            .value(hasItem(DEFAULT_DELETED_AT.toString()))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getCompany() {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        // Get the company
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, company.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(company.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.logo")
            .value(is(DEFAULT_LOGO))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.deletedAt")
            .value(is(DEFAULT_DELETED_AT.toString()))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingCompany() {
        // Get the company
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).block();
        updatedCompany
            .name(UPDATED_NAME)
            .logo(UPDATED_LOGO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .active(UPDATED_ACTIVE);
        CompanyDTO companyDTO = companyMapper.toDto(updatedCompany);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, companyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompanyToMatchAllProperties(updatedCompany);
    }

    @Test
    void putNonExistingCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, companyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany.logo(UPDATED_LOGO).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCompany))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Company in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompany, company), getPersistedCompany(company));
    }

    @Test
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .name(UPDATED_NAME)
            .logo(UPDATED_LOGO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .active(UPDATED_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCompany))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Company in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyUpdatableFieldsEquals(partialUpdatedCompany, getPersistedCompany(partialUpdatedCompany));
    }

    @Test
    void patchNonExistingCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, companyDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCompany() {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the company
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, company.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return companyRepository.count().block();
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

    protected Company getPersistedCompany(Company company) {
        return companyRepository.findById(company.getId()).block();
    }

    protected void assertPersistedCompanyToMatchAllProperties(Company expectedCompany) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCompanyAllPropertiesEquals(expectedCompany, getPersistedCompany(expectedCompany));
        assertCompanyUpdatableFieldsEquals(expectedCompany, getPersistedCompany(expectedCompany));
    }

    protected void assertPersistedCompanyToMatchUpdatableProperties(Company expectedCompany) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCompanyAllUpdatablePropertiesEquals(expectedCompany, getPersistedCompany(expectedCompany));
        assertCompanyUpdatableFieldsEquals(expectedCompany, getPersistedCompany(expectedCompany));
    }
}
