package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubmissionAuditDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubmissionAuditDTO.class);
        SubmissionAuditDTO submissionAuditDTO1 = new SubmissionAuditDTO();
        submissionAuditDTO1.setId(1L);
        SubmissionAuditDTO submissionAuditDTO2 = new SubmissionAuditDTO();
        assertThat(submissionAuditDTO1).isNotEqualTo(submissionAuditDTO2);
        submissionAuditDTO2.setId(submissionAuditDTO1.getId());
        assertThat(submissionAuditDTO1).isEqualTo(submissionAuditDTO2);
        submissionAuditDTO2.setId(2L);
        assertThat(submissionAuditDTO1).isNotEqualTo(submissionAuditDTO2);
        submissionAuditDTO1.setId(null);
        assertThat(submissionAuditDTO1).isNotEqualTo(submissionAuditDTO2);
    }
}
