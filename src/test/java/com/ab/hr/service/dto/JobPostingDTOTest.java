package com.ab.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobPostingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobPostingDTO.class);
        JobPostingDTO jobPostingDTO1 = new JobPostingDTO();
        jobPostingDTO1.setId(1L);
        JobPostingDTO jobPostingDTO2 = new JobPostingDTO();
        assertThat(jobPostingDTO1).isNotEqualTo(jobPostingDTO2);
        jobPostingDTO2.setId(jobPostingDTO1.getId());
        assertThat(jobPostingDTO1).isEqualTo(jobPostingDTO2);
        jobPostingDTO2.setId(2L);
        assertThat(jobPostingDTO1).isNotEqualTo(jobPostingDTO2);
        jobPostingDTO1.setId(null);
        assertThat(jobPostingDTO1).isNotEqualTo(jobPostingDTO2);
    }
}
