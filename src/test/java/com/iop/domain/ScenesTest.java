package com.iop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.iop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScenesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scenes.class);
        Scenes scenes1 = new Scenes();
        scenes1.setId(1L);
        Scenes scenes2 = new Scenes();
        scenes2.setId(scenes1.getId());
        assertThat(scenes1).isEqualTo(scenes2);
        scenes2.setId(2L);
        assertThat(scenes1).isNotEqualTo(scenes2);
        scenes1.setId(null);
        assertThat(scenes1).isNotEqualTo(scenes2);
    }
}
