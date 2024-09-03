package com.ab.hr.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ab.hr.domain.UserAssignment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAssignmentDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant assignedAt;

    private Instant joinedAt;

    private Instant finishedAt;

    private UserDTO user;

    private AssignmentDTO assignment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AssignmentDTO getAssignment() {
        return assignment;
    }

    public void setAssignment(AssignmentDTO assignment) {
        this.assignment = assignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAssignmentDTO)) {
            return false;
        }

        UserAssignmentDTO userAssignmentDTO = (UserAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAssignmentDTO{" +
            "id=" + getId() +
            ", assignedAt='" + getAssignedAt() + "'" +
            ", joinedAt='" + getJoinedAt() + "'" +
            ", finishedAt='" + getFinishedAt() + "'" +
            ", user=" + getUser() +
            ", assignment=" + getAssignment() +
            "}";
    }
}
