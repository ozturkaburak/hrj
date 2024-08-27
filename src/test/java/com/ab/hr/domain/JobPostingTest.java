package com.ab.hr.domain;

import static com.ab.hr.domain.JobPostingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobPostingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobPosting.class);
        JobPosting jobPosting1 = getJobPostingSample1();
        JobPosting jobPosting2 = new JobPosting();
        assertThat(jobPosting1).isNotEqualTo(jobPosting2);

        jobPosting2.setId(jobPosting1.getId());
        assertThat(jobPosting1).isEqualTo(jobPosting2);

        jobPosting2 = getJobPostingSample2();
        assertThat(jobPosting1).isNotEqualTo(jobPosting2);
    }
}
