package com.sheetsquad.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheetsquad.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BaseExtraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaseExtra.class);
        BaseExtra baseExtra1 = new BaseExtra();
        baseExtra1.setId(1L);
        BaseExtra baseExtra2 = new BaseExtra();
        baseExtra2.setId(baseExtra1.getId());
        assertThat(baseExtra1).isEqualTo(baseExtra2);
        baseExtra2.setId(2L);
        assertThat(baseExtra1).isNotEqualTo(baseExtra2);
        baseExtra1.setId(null);
        assertThat(baseExtra1).isNotEqualTo(baseExtra2);
    }
}
