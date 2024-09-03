package com.ab.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExperienceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExperienceDTO.class);
        ExperienceDTO experienceDTO1 = new ExperienceDTO();
        experienceDTO1.setId(1L);
        ExperienceDTO experienceDTO2 = new ExperienceDTO();
        assertThat(experienceDTO1).isNotEqualTo(experienceDTO2);
        experienceDTO2.setId(experienceDTO1.getId());
        assertThat(experienceDTO1).isEqualTo(experienceDTO2);
        experienceDTO2.setId(2L);
        assertThat(experienceDTO1).isNotEqualTo(experienceDTO2);
        experienceDTO1.setId(null);
        assertThat(experienceDTO1).isNotEqualTo(experienceDTO2);
    }
}
