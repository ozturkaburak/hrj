package com.ab.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EducationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationDTO.class);
        EducationDTO educationDTO1 = new EducationDTO();
        educationDTO1.setId(1L);
        EducationDTO educationDTO2 = new EducationDTO();
        assertThat(educationDTO1).isNotEqualTo(educationDTO2);
        educationDTO2.setId(educationDTO1.getId());
        assertThat(educationDTO1).isEqualTo(educationDTO2);
        educationDTO2.setId(2L);
        assertThat(educationDTO1).isNotEqualTo(educationDTO2);
        educationDTO1.setId(null);
        assertThat(educationDTO1).isNotEqualTo(educationDTO2);
    }
}
