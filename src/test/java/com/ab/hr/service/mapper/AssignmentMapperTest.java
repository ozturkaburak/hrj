package com.ab.hr.service.mapper;

import static com.ab.hr.domain.AssignmentAsserts.*;
import static com.ab.hr.domain.AssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssignmentMapperTest {

    private AssignmentMapper assignmentMapper;

    @BeforeEach
    void setUp() {
        assignmentMapper = new AssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssignmentSample1();
        var actual = assignmentMapper.toEntity(assignmentMapper.toDto(expected));
        assertAssignmentAllPropertiesEquals(expected, actual);
    }
}
