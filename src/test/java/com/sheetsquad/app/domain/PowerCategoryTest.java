package com.sheetsquad.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheetsquad.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PowerCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PowerCategory.class);
        PowerCategory powerCategory1 = new PowerCategory();
        powerCategory1.setId(1L);
        PowerCategory powerCategory2 = new PowerCategory();
        powerCategory2.setId(powerCategory1.getId());
        assertThat(powerCategory1).isEqualTo(powerCategory2);
        powerCategory2.setId(2L);
        assertThat(powerCategory1).isNotEqualTo(powerCategory2);
        powerCategory1.setId(null);
        assertThat(powerCategory1).isNotEqualTo(powerCategory2);
    }
}
