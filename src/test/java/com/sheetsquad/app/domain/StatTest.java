package com.sheetsquad.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheetsquad.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stat.class);
        Stat stat1 = new Stat();
        stat1.setId(1L);
        Stat stat2 = new Stat();
        stat2.setId(stat1.getId());
        assertThat(stat1).isEqualTo(stat2);
        stat2.setId(2L);
        assertThat(stat1).isNotEqualTo(stat2);
        stat1.setId(null);
        assertThat(stat1).isNotEqualTo(stat2);
    }
}
