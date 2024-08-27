package com.ab.hr.domain;

import com.ab.hr.domain.enumeration.JobStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A JobPosting.
 */
@Table("job_posting")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobPosting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Column("description")
    private String description;

    @Column("location")
    private String location;

    @Column("department")
    private String department;

    @Column("status")
    private JobStatus status;

    @Column("created_date")
    private Instant createdDate;

    @Column("expire_date")
    private Instant expireDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JobPosting id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public JobPosting title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public JobPosting description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public JobPosting location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return this.department;
    }

    public JobPosting department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public JobStatus getStatus() {
        return this.status;
    }

    public JobPosting status(JobStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public JobPosting createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getExpireDate() {
        return this.expireDate;
    }

    public JobPosting expireDate(Instant expireDate) {
        this.setExpireDate(expireDate);
        return this;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobPosting)) {
            return false;
        }
        return getId() != null && getId().equals(((JobPosting) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobPosting{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            ", department='" + getDepartment() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", expireDate='" + getExpireDate() + "'" +
            "}";
    }
}
