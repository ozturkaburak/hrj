package com.ab.hr.domain;

import static com.ab.hr.domain.AssignmentTestSamples.*;
import static com.ab.hr.domain.QuestionTestSamples.*;
import static com.ab.hr.domain.UserAssignmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assignment.class);
        Assignment assignment1 = getAssignmentSample1();
        Assignment assignment2 = new Assignment();
        assertThat(assignment1).isNotEqualTo(assignment2);

        assignment2.setId(assignment1.getId());
        assertThat(assignment1).isEqualTo(assignment2);

        assignment2 = getAssignmentSample2();
        assertThat(assignment1).isNotEqualTo(assignment2);
    }

    @Test
    void questionsTest() {
        Assignment assignment = getAssignmentRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        assignment.addQuestions(questionBack);
        assertThat(assignment.getQuestions()).containsOnly(questionBack);

        assignment.removeQuestions(questionBack);
        assertThat(assignment.getQuestions()).doesNotContain(questionBack);

        assignment.questions(new HashSet<>(Set.of(questionBack)));
        assertThat(assignment.getQuestions()).containsOnly(questionBack);

        assignment.setQuestions(new HashSet<>());
        assertThat(assignment.getQuestions()).doesNotContain(questionBack);
    }

    @Test
    void userAssignmentTest() {
        Assignment assignment = getAssignmentRandomSampleGenerator();
        UserAssignment userAssignmentBack = getUserAssignmentRandomSampleGenerator();

        assignment.setUserAssignment(userAssignmentBack);
        assertThat(assignment.getUserAssignment()).isEqualTo(userAssignmentBack);
        assertThat(userAssignmentBack.getAssignment()).isEqualTo(assignment);

        assignment.userAssignment(null);
        assertThat(assignment.getUserAssignment()).isNull();
        assertThat(userAssignmentBack.getAssignment()).isNull();
    }
}
