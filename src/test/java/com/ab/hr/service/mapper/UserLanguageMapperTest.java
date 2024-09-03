package com.ab.hr.service.mapper;

import static com.ab.hr.domain.UserLanguageAsserts.*;
import static com.ab.hr.domain.UserLanguageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserLanguageMapperTest {

    private UserLanguageMapper userLanguageMapper;

    @BeforeEach
    void setUp() {
        userLanguageMapper = new UserLanguageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserLanguageSample1();
        var actual = userLanguageMapper.toEntity(userLanguageMapper.toDto(expected));
        assertUserLanguageAllPropertiesEquals(expected, actual);
    }
}
