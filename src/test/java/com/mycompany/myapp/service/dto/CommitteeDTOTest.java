package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommitteeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommitteeDTO.class);
        CommitteeDTO committeeDTO1 = new CommitteeDTO();
        committeeDTO1.setId(1L);
        CommitteeDTO committeeDTO2 = new CommitteeDTO();
        assertThat(committeeDTO1).isNotEqualTo(committeeDTO2);
        committeeDTO2.setId(committeeDTO1.getId());
        assertThat(committeeDTO1).isEqualTo(committeeDTO2);
        committeeDTO2.setId(2L);
        assertThat(committeeDTO1).isNotEqualTo(committeeDTO2);
        committeeDTO1.setId(null);
        assertThat(committeeDTO1).isNotEqualTo(committeeDTO2);
    }
}
