package com.ab.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UploadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UploadDTO.class);
        UploadDTO uploadDTO1 = new UploadDTO();
        uploadDTO1.setId(1L);
        UploadDTO uploadDTO2 = new UploadDTO();
        assertThat(uploadDTO1).isNotEqualTo(uploadDTO2);
        uploadDTO2.setId(uploadDTO1.getId());
        assertThat(uploadDTO1).isEqualTo(uploadDTO2);
        uploadDTO2.setId(2L);
        assertThat(uploadDTO1).isNotEqualTo(uploadDTO2);
        uploadDTO1.setId(null);
        assertThat(uploadDTO1).isNotEqualTo(uploadDTO2);
    }
}
