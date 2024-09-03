package com.ab.hr.service.mapper;

import static com.ab.hr.domain.EducationAsserts.*;
import static com.ab.hr.domain.EducationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EducationMapperTest {

    private EducationMapper educationMapper;

    @BeforeEach
    void setUp() {
        educationMapper = new EducationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEducationSample1();
        var actual = educationMapper.toEntity(educationMapper.toDto(expected));
        assertEducationAllPropertiesEquals(expected, actual);
    }
}
