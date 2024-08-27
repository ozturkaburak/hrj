package com.ab.hr.service.mapper;

import static com.ab.hr.domain.JobPostingAsserts.*;
import static com.ab.hr.domain.JobPostingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobPostingMapperTest {

    private JobPostingMapper jobPostingMapper;

    @BeforeEach
    void setUp() {
        jobPostingMapper = new JobPostingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getJobPostingSample1();
        var actual = jobPostingMapper.toEntity(jobPostingMapper.toDto(expected));
        assertJobPostingAllPropertiesEquals(expected, actual);
    }
}
