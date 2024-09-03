package com.ab.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AssignmentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentAllPropertiesEquals(Assignment expected, Assignment actual) {
        assertAssignmentAutoGeneratedPropertiesEquals(expected, actual);
        assertAssignmentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentAllUpdatablePropertiesEquals(Assignment expected, Assignment actual) {
        assertAssignmentUpdatableFieldsEquals(expected, actual);
        assertAssignmentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentAutoGeneratedPropertiesEquals(Assignment expected, Assignment actual) {
        assertThat(expected)
            .as("Verify Assignment auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentUpdatableFieldsEquals(Assignment expected, Assignment actual) {
        assertThat(expected)
            .as("Verify Assignment relevant properties")
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getVisible()).as("check visible").isEqualTo(actual.getVisible()))
            .satisfies(e -> assertThat(e.getHashtags()).as("check hashtags").isEqualTo(actual.getHashtags()))
            .satisfies(e -> assertThat(e.getCreatedAt()).as("check createdAt").isEqualTo(actual.getCreatedAt()))
            .satisfies(e -> assertThat(e.getUpdatedAt()).as("check updatedAt").isEqualTo(actual.getUpdatedAt()))
            .satisfies(e -> assertThat(e.getDeletedAt()).as("check deletedAt").isEqualTo(actual.getDeletedAt()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentUpdatableRelationshipsEquals(Assignment expected, Assignment actual) {
        assertThat(expected)
            .as("Verify Assignment relationships")
            .satisfies(e -> assertThat(e.getQuestions()).as("check questions").isEqualTo(actual.getQuestions()));
    }
}
