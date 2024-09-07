package com.ab.hr.domain.enumeration;

/**
 * The UserAssignmentStatus enumeration.
 */
public enum UserAssignmentStatus {
    /**
     * 
     * NOT_STARTED: The user has been assigned the task but has not yet begun it.

     */
    NOT_STARTED,
    /**
     * 
     * IN_PROGRESS: The user has started working on the assignment but has not completed it.

     */
    IN_PROGRESS,
    /**
     * 
     * COMPLETED: The user has successfully completed the assignment.

     */
    COMPLETED,
    /**
     * 
     * OVERDUE: The assignment has passed its due date without being completed.

     */
    OVERDUE,
}
