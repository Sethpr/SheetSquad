package com.sheetsquad.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheetsquad.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RefrenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Refrence.class);
        Refrence refrence1 = new Refrence();
        refrence1.setId(1L);
        Refrence refrence2 = new Refrence();
        refrence2.setId(refrence1.getId());
        assertThat(refrence1).isEqualTo(refrence2);
        refrence2.setId(2L);
        assertThat(refrence1).isNotEqualTo(refrence2);
        refrence1.setId(null);
        assertThat(refrence1).isNotEqualTo(refrence2);
    }
}
