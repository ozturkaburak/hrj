package com.ab.hr.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UserAssignment.
 */
@Table("user_assignment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("assigned_at")
    private Instant assignedAt;

    @Column("joined_at")
    private Instant joinedAt;

    @Column("finished_at")
    private Instant finishedAt;

    @Transient
    private User user;

    @Transient
    private Assignment assignment;

    @Column("user_id")
    private Long userId;

    @Column("assignment_id")
    private Long assignmentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAssignedAt() {
        return this.assignedAt;
    }

    public UserAssignment assignedAt(Instant assignedAt) {
        this.setAssignedAt(assignedAt);
        return this;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Instant getJoinedAt() {
        return this.joinedAt;
    }

    public UserAssignment joinedAt(Instant joinedAt) {
        this.setJoinedAt(joinedAt);
        return this;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Instant getFinishedAt() {
        return this.finishedAt;
    }

    public UserAssignment finishedAt(Instant finishedAt) {
        this.setFinishedAt(finishedAt);
        return this;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public UserAssignment user(User user) {
        this.setUser(user);
        return this;
    }

    public Assignment getAssignment() {
        return this.assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
        this.assignmentId = assignment != null ? assignment.getId() : null;
    }

    public UserAssignment assignment(Assignment assignment) {
        this.setAssignment(assignment);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long user) {
        this.userId = user;
    }

    public Long getAssignmentId() {
        return this.assignmentId;
    }

    public void setAssignmentId(Long assignment) {
        this.assignmentId = assignment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAssignment)) {
            return false;
        }
        return getId() != null && getId().equals(((UserAssignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAssignment{" +
            "id=" + getId() +
            ", assignedAt='" + getAssignedAt() + "'" +
            ", joinedAt='" + getJoinedAt() + "'" +
            ", finishedAt='" + getFinishedAt() + "'" +
            "}";
    }
}
