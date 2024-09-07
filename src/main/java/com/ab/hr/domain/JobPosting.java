package com.ab.hr.domain;

import com.ab.hr.domain.enumeration.JobStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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

    @Column("status")
    private JobStatus status;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private Instant createdDate;

    @Column("expire_date")
    private Instant expireDate;

    @Transient
    private Company company;

    @Column("company_id")
    private Long companyId;

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

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
        this.companyId = company != null ? company.getId() : null;
    }

    public JobPosting company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long company) {
        this.companyId = company;
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
            ", status='" + getStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", expireDate='" + getExpireDate() + "'" +
            "}";
    }
}
