package com.amidi.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.amidi.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DishTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DishTag.class);
        DishTag dishTag1 = new DishTag();
        dishTag1.setId(1L);
        DishTag dishTag2 = new DishTag();
        dishTag2.setId(dishTag1.getId());
        assertThat(dishTag1).isEqualTo(dishTag2);
        dishTag2.setId(2L);
        assertThat(dishTag1).isNotEqualTo(dishTag2);
        dishTag1.setId(null);
        assertThat(dishTag1).isNotEqualTo(dishTag2);
    }
}
