package com.sheetsquad.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheetsquad.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QualityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quality.class);
        Quality quality1 = new Quality();
        quality1.setId(1L);
        Quality quality2 = new Quality();
        quality2.setId(quality1.getId());
        assertThat(quality1).isEqualTo(quality2);
        quality2.setId(2L);
        assertThat(quality1).isNotEqualTo(quality2);
        quality1.setId(null);
        assertThat(quality1).isNotEqualTo(quality2);
    }
}
