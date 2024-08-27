package com.ab.hr.service.dto;

import com.ab.hr.domain.enumeration.JobStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ab.hr.domain.JobPosting} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobPostingDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    @NotNull(message = "must not be null")
    private String description;

    private String location;

    private String department;

    private JobStatus status;

    private Instant createdDate;

    private Instant expireDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobPostingDTO)) {
            return false;
        }

        JobPostingDTO jobPostingDTO = (JobPostingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jobPostingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobPostingDTO{" +
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
