package com.ab.hr.service.mapper;

import static com.ab.hr.domain.UserSkillAsserts.*;
import static com.ab.hr.domain.UserSkillTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserSkillMapperTest {

    private UserSkillMapper userSkillMapper;

    @BeforeEach
    void setUp() {
        userSkillMapper = new UserSkillMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserSkillSample1();
        var actual = userSkillMapper.toEntity(userSkillMapper.toDto(expected));
        assertUserSkillAllPropertiesEquals(expected, actual);
    }
}
