package com.ab.hr.domain;

import com.ab.hr.domain.enumeration.FileExtention;
import com.ab.hr.domain.enumeration.FileType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Upload.
 */
@Table("upload")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Upload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("url")
    private String url;

    @NotNull(message = "must not be null")
    @Column("type")
    private FileType type;

    @NotNull(message = "must not be null")
    @Column("extension")
    private FileExtention extension;

    @NotNull(message = "must not be null")
    @Column("upload_date")
    private Instant uploadDate;

    @Transient
    @JsonIgnoreProperties(value = { "user", "experiences", "educations", "certificates", "aboutMes", "uploads" }, allowSetters = true)
    private UserProfile userProfile;

    @Column("user_profile_id")
    private Long userProfileId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Upload id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public Upload url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FileType getType() {
        return this.type;
    }

    public Upload type(FileType type) {
        this.setType(type);
        return this;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public FileExtention getExtension() {
        return this.extension;
    }

    public Upload extension(FileExtention extension) {
        this.setExtension(extension);
        return this;
    }

    public void setExtension(FileExtention extension) {
        this.extension = extension;
    }

    public Instant getUploadDate() {
        return this.uploadDate;
    }

    public Upload uploadDate(Instant uploadDate) {
        this.setUploadDate(uploadDate);
        return this;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        this.userProfileId = userProfile != null ? userProfile.getId() : null;
    }

    public Upload userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    public Long getUserProfileId() {
        return this.userProfileId;
    }

    public void setUserProfileId(Long userProfile) {
        this.userProfileId = userProfile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Upload)) {
            return false;
        }
        return getId() != null && getId().equals(((Upload) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Upload{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", type='" + getType() + "'" +
            ", extension='" + getExtension() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            "}";
    }
}
