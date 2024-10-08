package com.ab.hr.domain;

import com.ab.hr.domain.enumeration.ContractType;
import com.ab.hr.domain.enumeration.WorkType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Experience.
 */
@Table("experience")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Experience implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Column("work_type")
    private WorkType workType;

    @NotNull(message = "must not be null")
    @Column("contract_type")
    private ContractType contractType;

    @Column("start_date")
    private LocalDate startDate;

    @Column("end_date")
    private LocalDate endDate;

    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("deleted_at")
    private Instant deletedAt;

    @Transient
    private Company company;

    @Transient
    @JsonIgnoreProperties(value = { "experience", "userSkill" }, allowSetters = true)
    private Set<Skill> skills = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "user", "experiences", "educations", "certificates", "aboutMes", "uploads" }, allowSetters = true)
    private UserProfile userProfile;

    @Column("company_id")
    private Long companyId;

    @Column("user_profile_id")
    private Long userProfileId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Experience id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Experience title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public WorkType getWorkType() {
        return this.workType;
    }

    public Experience workType(WorkType workType) {
        this.setWorkType(workType);
        return this;
    }

    public void setWorkType(WorkType workType) {
        this.workType = workType;
    }

    public ContractType getContractType() {
        return this.contractType;
    }

    public Experience contractType(ContractType contractType) {
        this.setContractType(contractType);
        return this;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Experience startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Experience endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return this.description;
    }

    public Experience description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Experience createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Experience updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public Experience deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
        this.companyId = company != null ? company.getId() : null;
    }

    public Experience company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<Skill> getSkills() {
        return this.skills;
    }

    public void setSkills(Set<Skill> skills) {
        if (this.skills != null) {
            this.skills.forEach(i -> i.setExperience(null));
        }
        if (skills != null) {
            skills.forEach(i -> i.setExperience(this));
        }
        this.skills = skills;
    }

    public Experience skills(Set<Skill> skills) {
        this.setSkills(skills);
        return this;
    }

    public Experience addSkills(Skill skill) {
        this.skills.add(skill);
        skill.setExperience(this);
        return this;
    }

    public Experience removeSkills(Skill skill) {
        this.skills.remove(skill);
        skill.setExperience(null);
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        this.userProfileId = userProfile != null ? userProfile.getId() : null;
    }

    public Experience userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long company) {
        this.companyId = company;
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
        if (!(o instanceof Experience)) {
            return false;
        }
        return getId() != null && getId().equals(((Experience) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Experience{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", workType='" + getWorkType() + "'" +
            ", contractType='" + getContractType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
