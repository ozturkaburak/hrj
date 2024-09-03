package com.ab.hr.web.rest;

import static com.ab.hr.domain.ContactAsserts.*;
import static com.ab.hr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ab.hr.IntegrationTest;
import com.ab.hr.domain.Contact;
import com.ab.hr.repository.ContactRepository;
import com.ab.hr.repository.EntityManager;
import com.ab.hr.repository.UserRepository;
import com.ab.hr.service.dto.ContactDTO;
import com.ab.hr.service.mapper.ContactMapper;
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
 * Integration tests for the {@link ContactResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ContactResourceIT {

    private static final String DEFAULT_SECONDARY_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Contact contact;

    private Contact insertedContact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createEntity(EntityManager em) {
        Contact contact = new Contact()
            .secondaryEmail(DEFAULT_SECONDARY_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return contact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createUpdatedEntity(EntityManager em) {
        Contact contact = new Contact()
            .secondaryEmail(UPDATED_SECONDARY_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return contact;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Contact.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        contact = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedContact != null) {
            contactRepository.delete(insertedContact).block();
            insertedContact = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createContact() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);
        var returnedContactDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ContactDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Contact in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContact = contactMapper.toEntity(returnedContactDTO);
        assertContactUpdatableFieldsEquals(returnedContact, getPersistedContact(returnedContact));

        insertedContact = returnedContact;
    }

    @Test
    void createContactWithExistingId() throws Exception {
        // Create the Contact with an existing ID
        contact.setId(1L);
        ContactDTO contactDTO = contactMapper.toDto(contact);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Contact in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contact.setCreatedAt(null);

        // Create the Contact, which fails.
        ContactDTO contactDTO = contactMapper.toDto(contact);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllContacts() {
        // Initialize the database
        insertedContact = contactRepository.save(contact).block();

        // Get all the contactList
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
            .value(hasItem(contact.getId().intValue()))
            .jsonPath("$.[*].secondaryEmail")
            .value(hasItem(DEFAULT_SECONDARY_EMAIL))
            .jsonPath("$.[*].phoneNumber")
            .value(hasItem(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].deletedAt")
            .value(hasItem(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getContact() {
        // Initialize the database
        insertedContact = contactRepository.save(contact).block();

        // Get the contact
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, contact.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(contact.getId().intValue()))
            .jsonPath("$.secondaryEmail")
            .value(is(DEFAULT_SECONDARY_EMAIL))
            .jsonPath("$.phoneNumber")
            .value(is(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.deletedAt")
            .value(is(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getNonExistingContact() {
        // Get the contact
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingContact() throws Exception {
        // Initialize the database
        insertedContact = contactRepository.save(contact).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contact
        Contact updatedContact = contactRepository.findById(contact.getId()).block();
        updatedContact
            .secondaryEmail(UPDATED_SECONDARY_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        ContactDTO contactDTO = contactMapper.toDto(updatedContact);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, contactDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Contact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContactToMatchAllProperties(updatedContact);
    }

    @Test
    void putNonExistingContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, contactDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Contact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Contact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Contact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateContactWithPatch() throws Exception {
        // Initialize the database
        insertedContact = contactRepository.save(contact).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contact using partial update
        Contact partialUpdatedContact = new Contact();
        partialUpdatedContact.setId(contact.getId());

        partialUpdatedContact.secondaryEmail(UPDATED_SECONDARY_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER).createdAt(UPDATED_CREATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedContact.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedContact))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Contact in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContact, contact), getPersistedContact(contact));
    }

    @Test
    void fullUpdateContactWithPatch() throws Exception {
        // Initialize the database
        insertedContact = contactRepository.save(contact).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contact using partial update
        Contact partialUpdatedContact = new Contact();
        partialUpdatedContact.setId(contact.getId());

        partialUpdatedContact
            .secondaryEmail(UPDATED_SECONDARY_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedContact.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedContact))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Contact in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactUpdatableFieldsEquals(partialUpdatedContact, getPersistedContact(partialUpdatedContact));
    }

    @Test
    void patchNonExistingContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, contactDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Contact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Contact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Contact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteContact() {
        // Initialize the database
        insertedContact = contactRepository.save(contact).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contact
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, contact.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contactRepository.count().block();
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

    protected Contact getPersistedContact(Contact contact) {
        return contactRepository.findById(contact.getId()).block();
    }

    protected void assertPersistedContactToMatchAllProperties(Contact expectedContact) {
        // Test fails because reactive api returns an empty object instead of null
        // assertContactAllPropertiesEquals(expectedContact, getPersistedContact(expectedContact));
        assertContactUpdatableFieldsEquals(expectedContact, getPersistedContact(expectedContact));
    }

    protected void assertPersistedContactToMatchUpdatableProperties(Contact expectedContact) {
        // Test fails because reactive api returns an empty object instead of null
        // assertContactAllUpdatablePropertiesEquals(expectedContact, getPersistedContact(expectedContact));
        assertContactUpdatableFieldsEquals(expectedContact, getPersistedContact(expectedContact));
    }
}
