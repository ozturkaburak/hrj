package com.ab.hr.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Answer.
 */
@Table("answer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * given answer by a user. It can be either multiple/signle option(s), a text or URL of a video uploaded
     */
    @NotNull(message = "must not be null")
    @Column("content")
    private String content;

    @NotNull(message = "must not be null")
    @Column("answered_at")
    private ZonedDateTime answeredAt;

    @Transient
    private Question question;

    @Transient
    private User user;

    @Column("question_id")
    private Long questionId;

    @Column("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Answer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public Answer content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getAnsweredAt() {
        return this.answeredAt;
    }

    public Answer answeredAt(ZonedDateTime answeredAt) {
        this.setAnsweredAt(answeredAt);
        return this;
    }

    public void setAnsweredAt(ZonedDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
        this.questionId = question != null ? question.getId() : null;
    }

    public Answer question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public Answer user(User user) {
        this.setUser(user);
        return this;
    }

    public Long getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(Long question) {
        this.questionId = question;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long user) {
        this.userId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answer)) {
            return false;
        }
        return getId() != null && getId().equals(((Answer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Answer{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", answeredAt='" + getAnsweredAt() + "'" +
            "}";
    }
}
