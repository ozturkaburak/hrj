package com.ab.hr.domain;

import com.ab.hr.domain.enumeration.QuestionType;
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
 * A Question.
 */
@Table("question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("content")
    private String content;

    @Column("options")
    private String options;

    @NotNull(message = "must not be null")
    @Column("type")
    private QuestionType type;

    @Column("correct_answer")
    private String correctAnswer;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("deleted_at")
    private Instant deletedAt;

    @Transient
    @JsonIgnoreProperties(value = { "questions", "userAssignment" }, allowSetters = true)
    private Set<Assignment> assignments = new HashSet<>();

    @Transient
    private Answer answer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public Question content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOptions() {
        return this.options;
    }

    public Question options(String options) {
        this.setOptions(options);
        return this;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public QuestionType getType() {
        return this.type;
    }

    public Question type(QuestionType type) {
        this.setType(type);
        return this;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    public Question correctAnswer(String correctAnswer) {
        this.setCorrectAnswer(correctAnswer);
        return this;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Question createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Question updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public Question deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        if (this.assignments != null) {
            this.assignments.forEach(i -> i.removeQuestions(this));
        }
        if (assignments != null) {
            assignments.forEach(i -> i.addQuestions(this));
        }
        this.assignments = assignments;
    }

    public Question assignments(Set<Assignment> assignments) {
        this.setAssignments(assignments);
        return this;
    }

    public Question addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.getQuestions().add(this);
        return this;
    }

    public Question removeAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
        assignment.getQuestions().remove(this);
        return this;
    }

    public Answer getAnswer() {
        return this.answer;
    }

    public void setAnswer(Answer answer) {
        if (this.answer != null) {
            this.answer.setQuestion(null);
        }
        if (answer != null) {
            answer.setQuestion(this);
        }
        this.answer = answer;
    }

    public Question answer(Answer answer) {
        this.setAnswer(answer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return getId() != null && getId().equals(((Question) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", options='" + getOptions() + "'" +
            ", type='" + getType() + "'" +
            ", correctAnswer='" + getCorrectAnswer() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
