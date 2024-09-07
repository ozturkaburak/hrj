package com.ab.hr.domain;

import com.ab.hr.domain.enumeration.UserAssignmentStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
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

    /**
     * Questions will be shown in order by their ids. 102, 300, 200
     */
    @NotNull(message = "must not be null")
    @Column("order_of_questions")
    private String orderOfQuestions;

    /**
     * overwritten value of duration. if it's not provided default value will be used
     */
    @Column("total_duration_in_mins")
    private Integer totalDurationInMins;

    /**
     * this will be provided by the system when the assignment is assigned to a user. And will be sent to the user by Email, SMS, PN
     */
    @NotNull(message = "must not be null")
    @Column("access_url")
    private String accessUrl;

    /**
     * Expiry date for the access URL, after which the link is no longer valid
     */
    @Column("access_expiry_date")
    private Instant accessExpiryDate;

    @NotNull(message = "must not be null")
    @Column("user_assignment_status")
    private UserAssignmentStatus userAssignmentStatus;

    @NotNull(message = "must not be null")
    @Column("assigned_at")
    private Instant assignedAt;

    @Column("joined_at")
    private ZonedDateTime joinedAt;

    @Column("finished_at")
    private ZonedDateTime finishedAt;

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

    public String getOrderOfQuestions() {
        return this.orderOfQuestions;
    }

    public UserAssignment orderOfQuestions(String orderOfQuestions) {
        this.setOrderOfQuestions(orderOfQuestions);
        return this;
    }

    public void setOrderOfQuestions(String orderOfQuestions) {
        this.orderOfQuestions = orderOfQuestions;
    }

    public Integer getTotalDurationInMins() {
        return this.totalDurationInMins;
    }

    public UserAssignment totalDurationInMins(Integer totalDurationInMins) {
        this.setTotalDurationInMins(totalDurationInMins);
        return this;
    }

    public void setTotalDurationInMins(Integer totalDurationInMins) {
        this.totalDurationInMins = totalDurationInMins;
    }

    public String getAccessUrl() {
        return this.accessUrl;
    }

    public UserAssignment accessUrl(String accessUrl) {
        this.setAccessUrl(accessUrl);
        return this;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public Instant getAccessExpiryDate() {
        return this.accessExpiryDate;
    }

    public UserAssignment accessExpiryDate(Instant accessExpiryDate) {
        this.setAccessExpiryDate(accessExpiryDate);
        return this;
    }

    public void setAccessExpiryDate(Instant accessExpiryDate) {
        this.accessExpiryDate = accessExpiryDate;
    }

    public UserAssignmentStatus getUserAssignmentStatus() {
        return this.userAssignmentStatus;
    }

    public UserAssignment userAssignmentStatus(UserAssignmentStatus userAssignmentStatus) {
        this.setUserAssignmentStatus(userAssignmentStatus);
        return this;
    }

    public void setUserAssignmentStatus(UserAssignmentStatus userAssignmentStatus) {
        this.userAssignmentStatus = userAssignmentStatus;
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

    public ZonedDateTime getJoinedAt() {
        return this.joinedAt;
    }

    public UserAssignment joinedAt(ZonedDateTime joinedAt) {
        this.setJoinedAt(joinedAt);
        return this;
    }

    public void setJoinedAt(ZonedDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public ZonedDateTime getFinishedAt() {
        return this.finishedAt;
    }

    public UserAssignment finishedAt(ZonedDateTime finishedAt) {
        this.setFinishedAt(finishedAt);
        return this;
    }

    public void setFinishedAt(ZonedDateTime finishedAt) {
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
            ", orderOfQuestions='" + getOrderOfQuestions() + "'" +
            ", totalDurationInMins=" + getTotalDurationInMins() +
            ", accessUrl='" + getAccessUrl() + "'" +
            ", accessExpiryDate='" + getAccessExpiryDate() + "'" +
            ", userAssignmentStatus='" + getUserAssignmentStatus() + "'" +
            ", assignedAt='" + getAssignedAt() + "'" +
            ", joinedAt='" + getJoinedAt() + "'" +
            ", finishedAt='" + getFinishedAt() + "'" +
            "}";
    }
}
