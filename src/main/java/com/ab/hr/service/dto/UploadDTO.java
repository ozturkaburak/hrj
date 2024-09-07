package com.ab.hr.service.dto;

import com.ab.hr.domain.enumeration.FileExtention;
import com.ab.hr.domain.enumeration.FileType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ab.hr.domain.Upload} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UploadDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String url;

    @NotNull(message = "must not be null")
    private FileType type;

    @NotNull(message = "must not be null")
    private FileExtention extension;

    @NotNull(message = "must not be null")
    private Instant uploadDate;

    private UserProfileDTO userProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public FileExtention getExtension() {
        return extension;
    }

    public void setExtension(FileExtention extension) {
        this.extension = extension;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
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
        if (!(o instanceof UploadDTO)) {
            return false;
        }

        UploadDTO uploadDTO = (UploadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uploadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UploadDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", type='" + getType() + "'" +
            ", extension='" + getExtension() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            ", userProfile=" + getUserProfile() +
            "}";
    }
}
