package com.ab.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ResumeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertResumeAllPropertiesEquals(Resume expected, Resume actual) {
        assertResumeAutoGeneratedPropertiesEquals(expected, actual);
        assertResumeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertResumeAllUpdatablePropertiesEquals(Resume expected, Resume actual) {
        assertResumeUpdatableFieldsEquals(expected, actual);
        assertResumeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertResumeAutoGeneratedPropertiesEquals(Resume expected, Resume actual) {
        assertThat(expected)
            .as("Verify Resume auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertResumeUpdatableFieldsEquals(Resume expected, Resume actual) {
        assertThat(expected)
            .as("Verify Resume relevant properties")
            .satisfies(e -> assertThat(e.getFile()).as("check file").isEqualTo(actual.getFile()))
            .satisfies(e -> assertThat(e.getFileContentType()).as("check file contenty type").isEqualTo(actual.getFileContentType()))
            .satisfies(e -> assertThat(e.getFileType()).as("check fileType").isEqualTo(actual.getFileType()))
            .satisfies(e -> assertThat(e.getUploadDate()).as("check uploadDate").isEqualTo(actual.getUploadDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertResumeUpdatableRelationshipsEquals(Resume expected, Resume actual) {}
}
