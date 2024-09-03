package com.ab.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAssignmentDTO.class);
        UserAssignmentDTO userAssignmentDTO1 = new UserAssignmentDTO();
        userAssignmentDTO1.setId(1L);
        UserAssignmentDTO userAssignmentDTO2 = new UserAssignmentDTO();
        assertThat(userAssignmentDTO1).isNotEqualTo(userAssignmentDTO2);
        userAssignmentDTO2.setId(userAssignmentDTO1.getId());
        assertThat(userAssignmentDTO1).isEqualTo(userAssignmentDTO2);
        userAssignmentDTO2.setId(2L);
        assertThat(userAssignmentDTO1).isNotEqualTo(userAssignmentDTO2);
        userAssignmentDTO1.setId(null);
        assertThat(userAssignmentDTO1).isNotEqualTo(userAssignmentDTO2);
    }
}
