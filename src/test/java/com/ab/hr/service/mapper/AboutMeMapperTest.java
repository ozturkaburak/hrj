package com.ab.hr.service.mapper;

import static com.ab.hr.domain.AboutMeAsserts.*;
import static com.ab.hr.domain.AboutMeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AboutMeMapperTest {

    private AboutMeMapper aboutMeMapper;

    @BeforeEach
    void setUp() {
        aboutMeMapper = new AboutMeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAboutMeSample1();
        var actual = aboutMeMapper.toEntity(aboutMeMapper.toDto(expected));
        assertAboutMeAllPropertiesEquals(expected, actual);
    }
}
