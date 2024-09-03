package com.ab.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AboutMeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AboutMeDTO.class);
        AboutMeDTO aboutMeDTO1 = new AboutMeDTO();
        aboutMeDTO1.setId(1L);
        AboutMeDTO aboutMeDTO2 = new AboutMeDTO();
        assertThat(aboutMeDTO1).isNotEqualTo(aboutMeDTO2);
        aboutMeDTO2.setId(aboutMeDTO1.getId());
        assertThat(aboutMeDTO1).isEqualTo(aboutMeDTO2);
        aboutMeDTO2.setId(2L);
        assertThat(aboutMeDTO1).isNotEqualTo(aboutMeDTO2);
        aboutMeDTO1.setId(null);
        assertThat(aboutMeDTO1).isNotEqualTo(aboutMeDTO2);
    }
}
