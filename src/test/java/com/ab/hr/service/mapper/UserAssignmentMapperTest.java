package com.ab.hr.service.mapper;

import static com.ab.hr.domain.UserAssignmentAsserts.*;
import static com.ab.hr.domain.UserAssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAssignmentMapperTest {

    private UserAssignmentMapper userAssignmentMapper;

    @BeforeEach
    void setUp() {
        userAssignmentMapper = new UserAssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserAssignmentSample1();
        var actual = userAssignmentMapper.toEntity(userAssignmentMapper.toDto(expected));
        assertUserAssignmentAllPropertiesEquals(expected, actual);
    }
}
