package com.ab.hr.domain.enumeration;

/**
 * The QuestionType enumeration.
 */
public enum QuestionType {
    /**
     * 
     * SINGLE_CHOICE: A question with only one correct answer, where the user selects a single option.

     */
    SINGLE_CHOICE,
    /**
     * 
     * MULTIPLE_CHOICE: A question with multiple possible correct answers, where the user can select more than one option.

     */
    MULTIPLE_CHOICE,
    /**
     * 
     * TEXT: A question that requires the user to provide a written response.

     */
    TEXT,
    /**
     * 
     * VIDEO: A question where the user is required to provide a video response.

     */
    VIDEO,
}
