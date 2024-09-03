package com.ab.hr.domain;

import static com.ab.hr.domain.AssignmentTestSamples.*;
import static com.ab.hr.domain.UserAssignmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAssignment.class);
        UserAssignment userAssignment1 = getUserAssignmentSample1();
        UserAssignment userAssignment2 = new UserAssignment();
        assertThat(userAssignment1).isNotEqualTo(userAssignment2);

        userAssignment2.setId(userAssignment1.getId());
        assertThat(userAssignment1).isEqualTo(userAssignment2);

        userAssignment2 = getUserAssignmentSample2();
        assertThat(userAssignment1).isNotEqualTo(userAssignment2);
    }

    @Test
    void assignmentTest() {
        UserAssignment userAssignment = getUserAssignmentRandomSampleGenerator();
        Assignment assignmentBack = getAssignmentRandomSampleGenerator();

        userAssignment.setAssignment(assignmentBack);
        assertThat(userAssignment.getAssignment()).isEqualTo(assignmentBack);

        userAssignment.assignment(null);
        assertThat(userAssignment.getAssignment()).isNull();
    }
}
