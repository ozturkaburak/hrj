package com.ab.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserLanguageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserLanguageDTO.class);
        UserLanguageDTO userLanguageDTO1 = new UserLanguageDTO();
        userLanguageDTO1.setId(1L);
        UserLanguageDTO userLanguageDTO2 = new UserLanguageDTO();
        assertThat(userLanguageDTO1).isNotEqualTo(userLanguageDTO2);
        userLanguageDTO2.setId(userLanguageDTO1.getId());
        assertThat(userLanguageDTO1).isEqualTo(userLanguageDTO2);
        userLanguageDTO2.setId(2L);
        assertThat(userLanguageDTO1).isNotEqualTo(userLanguageDTO2);
        userLanguageDTO1.setId(null);
        assertThat(userLanguageDTO1).isNotEqualTo(userLanguageDTO2);
    }
}
