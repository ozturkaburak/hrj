package com.ab.hr.service.dto;

import com.ab.hr.domain.enumeration.UserAssignmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.ab.hr.domain.UserAssignment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAssignmentDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Schema(description = "Questions will be shown in order by their ids. 102, 300, 200", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderOfQuestions;

    @Schema(description = "overwritten value of duration. if it's not provided default value will be used")
    private Integer totalDurationInMins;

    @NotNull(message = "must not be null")
    @Schema(
        description = "this will be provided by the system when the assignment is assigned to a user. And will be sent to the user by Email, SMS, PN",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String accessUrl;

    @Schema(description = "Expiry date for the access URL, after which the link is no longer valid")
    private Instant accessExpiryDate;

    @NotNull(message = "must not be null")
    private UserAssignmentStatus userAssignmentStatus;

    @NotNull(message = "must not be null")
    private Instant assignedAt;

    private ZonedDateTime joinedAt;

    private ZonedDateTime finishedAt;

    private UserDTO user;

    private AssignmentDTO assignment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderOfQuestions() {
        return orderOfQuestions;
    }

    public void setOrderOfQuestions(String orderOfQuestions) {
        this.orderOfQuestions = orderOfQuestions;
    }

    public Integer getTotalDurationInMins() {
        return totalDurationInMins;
    }

    public void setTotalDurationInMins(Integer totalDurationInMins) {
        this.totalDurationInMins = totalDurationInMins;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public Instant getAccessExpiryDate() {
        return accessExpiryDate;
    }

    public void setAccessExpiryDate(Instant accessExpiryDate) {
        this.accessExpiryDate = accessExpiryDate;
    }

    public UserAssignmentStatus getUserAssignmentStatus() {
        return userAssignmentStatus;
    }

    public void setUserAssignmentStatus(UserAssignmentStatus userAssignmentStatus) {
        this.userAssignmentStatus = userAssignmentStatus;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public ZonedDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(ZonedDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public ZonedDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(ZonedDateTime finishedAt) {
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
            ", orderOfQuestions='" + getOrderOfQuestions() + "'" +
            ", totalDurationInMins=" + getTotalDurationInMins() +
            ", accessUrl='" + getAccessUrl() + "'" +
            ", accessExpiryDate='" + getAccessExpiryDate() + "'" +
            ", userAssignmentStatus='" + getUserAssignmentStatus() + "'" +
            ", assignedAt='" + getAssignedAt() + "'" +
            ", joinedAt='" + getJoinedAt() + "'" +
            ", finishedAt='" + getFinishedAt() + "'" +
            ", user=" + getUser() +
            ", assignment=" + getAssignment() +
            "}";
    }
}
