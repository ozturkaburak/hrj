package com.ab.hr.domain;

import static com.ab.hr.domain.AnswerTestSamples.*;
import static com.ab.hr.domain.AssignmentTestSamples.*;
import static com.ab.hr.domain.QuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = getQuestionSample1();
        Question question2 = new Question();
        assertThat(question1).isNotEqualTo(question2);

        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);

        question2 = getQuestionSample2();
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    void assignmentTest() {
        Question question = getQuestionRandomSampleGenerator();
        Assignment assignmentBack = getAssignmentRandomSampleGenerator();

        question.addAssignment(assignmentBack);
        assertThat(question.getAssignments()).containsOnly(assignmentBack);
        assertThat(assignmentBack.getQuestions()).containsOnly(question);

        question.removeAssignment(assignmentBack);
        assertThat(question.getAssignments()).doesNotContain(assignmentBack);
        assertThat(assignmentBack.getQuestions()).doesNotContain(question);

        question.assignments(new HashSet<>(Set.of(assignmentBack)));
        assertThat(question.getAssignments()).containsOnly(assignmentBack);
        assertThat(assignmentBack.getQuestions()).containsOnly(question);

        question.setAssignments(new HashSet<>());
        assertThat(question.getAssignments()).doesNotContain(assignmentBack);
        assertThat(assignmentBack.getQuestions()).doesNotContain(question);
    }

    @Test
    void answerTest() {
        Question question = getQuestionRandomSampleGenerator();
        Answer answerBack = getAnswerRandomSampleGenerator();

        question.setAnswer(answerBack);
        assertThat(question.getAnswer()).isEqualTo(answerBack);
        assertThat(answerBack.getQuestion()).isEqualTo(question);

        question.answer(null);
        assertThat(question.getAnswer()).isNull();
        assertThat(answerBack.getQuestion()).isNull();
    }
}
