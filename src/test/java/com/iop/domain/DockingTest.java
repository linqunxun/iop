package com.iop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.iop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DockingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Docking.class);
        Docking docking1 = new Docking();
        docking1.setId(1L);
        Docking docking2 = new Docking();
        docking2.setId(docking1.getId());
        assertThat(docking1).isEqualTo(docking2);
        docking2.setId(2L);
        assertThat(docking1).isNotEqualTo(docking2);
        docking1.setId(null);
        assertThat(docking1).isNotEqualTo(docking2);
    }
}
