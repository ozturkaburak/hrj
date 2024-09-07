package com.ab.hr.service.dto;

import com.ab.hr.domain.enumeration.LanguageLevel;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ab.hr.domain.UserLanguage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserLanguageDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private LanguageLevel level;

    @NotNull(message = "must not be null")
    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

    private UserDTO user;

    private LanguageDTO language;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LanguageLevel getLevel() {
        return level;
    }

    public void setLevel(LanguageLevel level) {
        this.level = level;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public LanguageDTO getLanguage() {
        return language;
    }

    public void setLanguage(LanguageDTO language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserLanguageDTO)) {
            return false;
        }

        UserLanguageDTO userLanguageDTO = (UserLanguageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userLanguageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserLanguageDTO{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", user=" + getUser() +
            ", language=" + getLanguage() +
            "}";
    }
}
