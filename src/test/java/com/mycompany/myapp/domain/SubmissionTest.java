package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CommitteeTestSamples.*;
import static com.mycompany.myapp.domain.GradeTestSamples.*;
import static com.mycompany.myapp.domain.PositionTestSamples.*;
import static com.mycompany.myapp.domain.SubmissionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubmissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Submission.class);
        Submission submission1 = getSubmissionSample1();
        Submission submission2 = new Submission();
        assertThat(submission1).isNotEqualTo(submission2);

        submission2.setId(submission1.getId());
        assertThat(submission1).isEqualTo(submission2);

        submission2 = getSubmissionSample2();
        assertThat(submission1).isNotEqualTo(submission2);
    }

    @Test
    void positionTest() {
        Submission submission = getSubmissionRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        submission.setPosition(positionBack);
        assertThat(submission.getPosition()).isEqualTo(positionBack);

        submission.position(null);
        assertThat(submission.getPosition()).isNull();
    }

    @Test
    void gradeTest() {
        Submission submission = getSubmissionRandomSampleGenerator();
        Grade gradeBack = getGradeRandomSampleGenerator();

        submission.setGrade(gradeBack);
        assertThat(submission.getGrade()).isEqualTo(gradeBack);

        submission.grade(null);
        assertThat(submission.getGrade()).isNull();
    }

    @Test
    void committeeNameTest() {
        Submission submission = getSubmissionRandomSampleGenerator();
        Committee committeeBack = getCommitteeRandomSampleGenerator();

        submission.setCommitteeName(committeeBack);
        assertThat(submission.getCommitteeName()).isEqualTo(committeeBack);

        submission.committeeName(null);
        assertThat(submission.getCommitteeName()).isNull();
    }
}
