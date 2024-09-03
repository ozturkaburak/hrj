package com.ab.hr.domain;

import com.ab.hr.domain.enumeration.AssignmentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Assignment.
 */
@Table("assignment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("type")
    private AssignmentType type;

    @NotNull(message = "must not be null")
    @Column("visible")
    private Boolean visible;

    @Column("hashtags")
    private String hashtags;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("deleted_at")
    private Instant deletedAt;

    @Transient
    @JsonIgnoreProperties(value = { "assignments", "answer" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    @Transient
    private UserAssignment userAssignment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Assignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AssignmentType getType() {
        return this.type;
    }

    public Assignment type(AssignmentType type) {
        this.setType(type);
        return this;
    }

    public void setType(AssignmentType type) {
        this.type = type;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public Assignment visible(Boolean visible) {
        this.setVisible(visible);
        return this;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getHashtags() {
        return this.hashtags;
    }

    public Assignment hashtags(String hashtags) {
        this.setHashtags(hashtags);
        return this;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Assignment createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Assignment updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public Assignment deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Assignment questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Assignment addQuestions(Question question) {
        this.questions.add(question);
        return this;
    }

    public Assignment removeQuestions(Question question) {
        this.questions.remove(question);
        return this;
    }

    public UserAssignment getUserAssignment() {
        return this.userAssignment;
    }

    public void setUserAssignment(UserAssignment userAssignment) {
        if (this.userAssignment != null) {
            this.userAssignment.setAssignment(null);
        }
        if (userAssignment != null) {
            userAssignment.setAssignment(this);
        }
        this.userAssignment = userAssignment;
    }

    public Assignment userAssignment(UserAssignment userAssignment) {
        this.setUserAssignment(userAssignment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assignment)) {
            return false;
        }
        return getId() != null && getId().equals(((Assignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assignment{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", visible='" + getVisible() + "'" +
            ", hashtags='" + getHashtags() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
