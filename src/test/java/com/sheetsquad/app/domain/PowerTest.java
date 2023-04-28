package com.sheetsquad.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheetsquad.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PowerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Power.class);
        Power power1 = new Power();
        power1.setId(1L);
        Power power2 = new Power();
        power2.setId(power1.getId());
        assertThat(power1).isEqualTo(power2);
        power2.setId(2L);
        assertThat(power1).isNotEqualTo(power2);
        power1.setId(null);
        assertThat(power1).isNotEqualTo(power2);
    }
}
