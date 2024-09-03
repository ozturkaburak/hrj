package com.ab.hr.service.dto;

import com.ab.hr.domain.enumeration.SkillLevel;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ab.hr.domain.UserSkill} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSkillDTO implements Serializable {

    private Long id;

    private Integer year;

    @NotNull(message = "must not be null")
    private SkillLevel level;

    @NotNull(message = "must not be null")
    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

    private SkillDTO skill;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public SkillLevel getLevel() {
        return level;
    }

    public void setLevel(SkillLevel level) {
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

    public SkillDTO getSkill() {
        return skill;
    }

    public void setSkill(SkillDTO skill) {
        this.skill = skill;
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
        if (!(o instanceof UserSkillDTO)) {
            return false;
        }

        UserSkillDTO userSkillDTO = (UserSkillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userSkillDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSkillDTO{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", level='" + getLevel() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", skill=" + getSkill() +
            ", user=" + getUser() +
            "}";
    }
}
