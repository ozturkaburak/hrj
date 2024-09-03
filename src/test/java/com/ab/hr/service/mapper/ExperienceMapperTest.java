package com.ab.hr.service.mapper;

import static com.ab.hr.domain.ExperienceAsserts.*;
import static com.ab.hr.domain.ExperienceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExperienceMapperTest {

    private ExperienceMapper experienceMapper;

    @BeforeEach
    void setUp() {
        experienceMapper = new ExperienceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExperienceSample1();
        var actual = experienceMapper.toEntity(experienceMapper.toDto(expected));
        assertExperienceAllPropertiesEquals(expected, actual);
    }
}
