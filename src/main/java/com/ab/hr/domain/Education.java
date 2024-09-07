package com.ab.hr.domain;

import com.ab.hr.domain.enumeration.EducationLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Education.
 */
@Table("education")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("faculty")
    private String faculty;

    @Column("level")
    private EducationLevel level;

    @Column("degree")
    private String degree;

    @NotNull(message = "must not be null")
    @Column("start_date")
    private LocalDate startDate;

    @Column("end_date")
    private Instant endDate;

    @Column("activities")
    private String activities;

    @Column("clubs")
    private String clubs;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("deleted_at")
    private Instant deletedAt;

    @Transient
    @JsonIgnoreProperties(value = { "user", "experiences", "educations", "certificates", "aboutMes", "uploads" }, allowSetters = true)
    private UserProfile userProfile;

    @Column("user_profile_id")
    private Long userProfileId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Education id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Education name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return this.faculty;
    }

    public Education faculty(String faculty) {
        this.setFaculty(faculty);
        return this;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public EducationLevel getLevel() {
        return this.level;
    }

    public Education level(EducationLevel level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(EducationLevel level) {
        this.level = level;
    }

    public String getDegree() {
        return this.degree;
    }

    public Education degree(String degree) {
        this.setDegree(degree);
        return this;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Education startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Education endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getActivities() {
        return this.activities;
    }

    public Education activities(String activities) {
        this.setActivities(activities);
        return this;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getClubs() {
        return this.clubs;
    }

    public Education clubs(String clubs) {
        this.setClubs(clubs);
        return this;
    }

    public void setClubs(String clubs) {
        this.clubs = clubs;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Education createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Education updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public Education deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        this.userProfileId = userProfile != null ? userProfile.getId() : null;
    }

    public Education userProfile(UserProfile userProfile) {
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
        if (!(o instanceof Education)) {
            return false;
        }
        return getId() != null && getId().equals(((Education) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Education{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", faculty='" + getFaculty() + "'" +
            ", level='" + getLevel() + "'" +
            ", degree='" + getDegree() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", activities='" + getActivities() + "'" +
            ", clubs='" + getClubs() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
