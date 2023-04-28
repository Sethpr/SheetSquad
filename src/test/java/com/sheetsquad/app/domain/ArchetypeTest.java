package com.sheetsquad.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheetsquad.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArchetypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Archetype.class);
        Archetype archetype1 = new Archetype();
        archetype1.setId(1L);
        Archetype archetype2 = new Archetype();
        archetype2.setId(archetype1.getId());
        assertThat(archetype1).isEqualTo(archetype2);
        archetype2.setId(2L);
        assertThat(archetype1).isNotEqualTo(archetype2);
        archetype1.setId(null);
        assertThat(archetype1).isNotEqualTo(archetype2);
    }
}
