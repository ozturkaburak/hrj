package com.ab.hr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.ab.hr.domain.Answer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnswerDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Schema(
        description = "given answer by a user. It can be either multiple/signle option(s), a text or URL of a video uploaded",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    @NotNull(message = "must not be null")
    private ZonedDateTime answeredAt;

    private QuestionDTO question;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(ZonedDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerDTO)) {
            return false;
        }

        AnswerDTO answerDTO = (AnswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, answerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", answeredAt='" + getAnsweredAt() + "'" +
            ", question=" + getQuestion() +
            ", user=" + getUser() +
            "}";
    }
}
