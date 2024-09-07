package com.ab.hr.domain;

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
 * A UserProfile.
 */
@Table("user_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("deleted_at")
    private Instant deletedAt;

    @Transient
    private User user;

    @Transient
    @JsonIgnoreProperties(value = { "company", "skills", "userProfile" }, allowSetters = true)
    private Set<Experience> experiences = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Set<Education> educations = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Set<Certificate> certificates = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Set<AboutMe> aboutMes = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Set<Upload> uploads = new HashSet<>();

    @Column("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UserProfile createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public UserProfile updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public UserProfile deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public UserProfile user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Experience> getExperiences() {
        return this.experiences;
    }

    public void setExperiences(Set<Experience> experiences) {
        if (this.experiences != null) {
            this.experiences.forEach(i -> i.setUserProfile(null));
        }
        if (experiences != null) {
            experiences.forEach(i -> i.setUserProfile(this));
        }
        this.experiences = experiences;
    }

    public UserProfile experiences(Set<Experience> experiences) {
        this.setExperiences(experiences);
        return this;
    }

    public UserProfile addExperiences(Experience experience) {
        this.experiences.add(experience);
        experience.setUserProfile(this);
        return this;
    }

    public UserProfile removeExperiences(Experience experience) {
        this.experiences.remove(experience);
        experience.setUserProfile(null);
        return this;
    }

    public Set<Education> getEducations() {
        return this.educations;
    }

    public void setEducations(Set<Education> educations) {
        if (this.educations != null) {
            this.educations.forEach(i -> i.setUserProfile(null));
        }
        if (educations != null) {
            educations.forEach(i -> i.setUserProfile(this));
        }
        this.educations = educations;
    }

    public UserProfile educations(Set<Education> educations) {
        this.setEducations(educations);
        return this;
    }

    public UserProfile addEducations(Education education) {
        this.educations.add(education);
        education.setUserProfile(this);
        return this;
    }

    public UserProfile removeEducations(Education education) {
        this.educations.remove(education);
        education.setUserProfile(null);
        return this;
    }

    public Set<Certificate> getCertificates() {
        return this.certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        if (this.certificates != null) {
            this.certificates.forEach(i -> i.setUserProfile(null));
        }
        if (certificates != null) {
            certificates.forEach(i -> i.setUserProfile(this));
        }
        this.certificates = certificates;
    }

    public UserProfile certificates(Set<Certificate> certificates) {
        this.setCertificates(certificates);
        return this;
    }

    public UserProfile addCertificates(Certificate certificate) {
        this.certificates.add(certificate);
        certificate.setUserProfile(this);
        return this;
    }

    public UserProfile removeCertificates(Certificate certificate) {
        this.certificates.remove(certificate);
        certificate.setUserProfile(null);
        return this;
    }

    public Set<AboutMe> getAboutMes() {
        return this.aboutMes;
    }

    public void setAboutMes(Set<AboutMe> aboutMes) {
        if (this.aboutMes != null) {
            this.aboutMes.forEach(i -> i.setUserProfile(null));
        }
        if (aboutMes != null) {
            aboutMes.forEach(i -> i.setUserProfile(this));
        }
        this.aboutMes = aboutMes;
    }

    public UserProfile aboutMes(Set<AboutMe> aboutMes) {
        this.setAboutMes(aboutMes);
        return this;
    }

    public UserProfile addAboutMe(AboutMe aboutMe) {
        this.aboutMes.add(aboutMe);
        aboutMe.setUserProfile(this);
        return this;
    }

    public UserProfile removeAboutMe(AboutMe aboutMe) {
        this.aboutMes.remove(aboutMe);
        aboutMe.setUserProfile(null);
        return this;
    }

    public Set<Upload> getUploads() {
        return this.uploads;
    }

    public void setUploads(Set<Upload> uploads) {
        if (this.uploads != null) {
            this.uploads.forEach(i -> i.setUserProfile(null));
        }
        if (uploads != null) {
            uploads.forEach(i -> i.setUserProfile(this));
        }
        this.uploads = uploads;
    }

    public UserProfile uploads(Set<Upload> uploads) {
        this.setUploads(uploads);
        return this;
    }

    public UserProfile addUploads(Upload upload) {
        this.uploads.add(upload);
        upload.setUserProfile(this);
        return this;
    }

    public UserProfile removeUploads(Upload upload) {
        this.uploads.remove(upload);
        upload.setUserProfile(null);
        return this;
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
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
