package com.amidi.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.amidi.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProUser.class);
        ProUser proUser1 = new ProUser();
        proUser1.setId(1L);
        ProUser proUser2 = new ProUser();
        proUser2.setId(proUser1.getId());
        assertThat(proUser1).isEqualTo(proUser2);
        proUser2.setId(2L);
        assertThat(proUser1).isNotEqualTo(proUser2);
        proUser1.setId(null);
        assertThat(proUser1).isNotEqualTo(proUser2);
    }
}
