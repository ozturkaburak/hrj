package com.ab.hr.service.dto;

import com.ab.hr.domain.enumeration.SocialMediaType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ab.hr.domain.AboutMe} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AboutMeDTO implements Serializable {

    private Long id;

    private SocialMediaType socialMedia;

    @NotNull(message = "must not be null")
    private String url;

    @NotNull(message = "must not be null")
    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

    private UserProfileDTO userProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SocialMediaType getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(SocialMediaType socialMedia) {
        this.socialMedia = socialMedia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public UserProfileDTO getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AboutMeDTO)) {
            return false;
        }

        AboutMeDTO aboutMeDTO = (AboutMeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aboutMeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AboutMeDTO{" +
            "id=" + getId() +
            ", socialMedia='" + getSocialMedia() + "'" +
            ", url='" + getUrl() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", userProfile=" + getUserProfile() +
            "}";
    }
}
