package com.ab.hr.service.mapper;

import static com.ab.hr.domain.ResumeAsserts.*;
import static com.ab.hr.domain.ResumeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResumeMapperTest {

    private ResumeMapper resumeMapper;

    @BeforeEach
    void setUp() {
        resumeMapper = new ResumeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getResumeSample1();
        var actual = resumeMapper.toEntity(resumeMapper.toDto(expected));
        assertResumeAllPropertiesEquals(expected, actual);
    }
}
