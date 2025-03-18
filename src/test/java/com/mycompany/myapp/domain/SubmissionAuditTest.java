package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SubmissionAuditTestSamples.*;
import static com.mycompany.myapp.domain.SubmissionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubmissionAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubmissionAudit.class);
        SubmissionAudit submissionAudit1 = getSubmissionAuditSample1();
        SubmissionAudit submissionAudit2 = new SubmissionAudit();
        assertThat(submissionAudit1).isNotEqualTo(submissionAudit2);

        submissionAudit2.setId(submissionAudit1.getId());
        assertThat(submissionAudit1).isEqualTo(submissionAudit2);

        submissionAudit2 = getSubmissionAuditSample2();
        assertThat(submissionAudit1).isNotEqualTo(submissionAudit2);
    }

    @Test
    void originalSubmissionTest() {
        SubmissionAudit submissionAudit = getSubmissionAuditRandomSampleGenerator();
        Submission submissionBack = getSubmissionRandomSampleGenerator();

        submissionAudit.setOriginalSubmission(submissionBack);
        assertThat(submissionAudit.getOriginalSubmission()).isEqualTo(submissionBack);

        submissionAudit.originalSubmission(null);
        assertThat(submissionAudit.getOriginalSubmission()).isNull();
    }
}
