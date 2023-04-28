package com.sheetsquad.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheetsquad.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExtraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Extra.class);
        Extra extra1 = new Extra();
        extra1.setId(1L);
        Extra extra2 = new Extra();
        extra2.setId(extra1.getId());
        assertThat(extra1).isEqualTo(extra2);
        extra2.setId(2L);
        assertThat(extra1).isNotEqualTo(extra2);
        extra1.setId(null);
        assertThat(extra1).isNotEqualTo(extra2);
    }
}
