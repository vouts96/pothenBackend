package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CommitteeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommitteeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Committee.class);
        Committee committee1 = getCommitteeSample1();
        Committee committee2 = new Committee();
        assertThat(committee1).isNotEqualTo(committee2);

        committee2.setId(committee1.getId());
        assertThat(committee1).isEqualTo(committee2);

        committee2 = getCommitteeSample2();
        assertThat(committee1).isNotEqualTo(committee2);
    }
}
