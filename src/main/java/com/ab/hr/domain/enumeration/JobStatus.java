package com.ab.hr.domain.enumeration;

/**
 * The JobStatus enumeration.
 */
public enum JobStatus {
    /**
     * 
     * OPEN: The job is currently available and accepting applications.

     */
    OPEN,
    /**
     * 
     * CLOSED: The job is no longer available and applications are no longer being accepted.

     */
    CLOSED,
    /**
     * 
     * FILLED: The job has been filled by a candidate.

     */
    FILLED,
}
