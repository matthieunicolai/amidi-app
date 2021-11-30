package com.amidi.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.amidi.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HedgeprodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hedgeprod.class);
        Hedgeprod hedgeprod1 = new Hedgeprod();
        hedgeprod1.setId(1L);
        Hedgeprod hedgeprod2 = new Hedgeprod();
        hedgeprod2.setId(hedgeprod1.getId());
        assertThat(hedgeprod1).isEqualTo(hedgeprod2);
        hedgeprod2.setId(2L);
        assertThat(hedgeprod1).isNotEqualTo(hedgeprod2);
        hedgeprod1.setId(null);
        assertThat(hedgeprod1).isNotEqualTo(hedgeprod2);
    }
}
