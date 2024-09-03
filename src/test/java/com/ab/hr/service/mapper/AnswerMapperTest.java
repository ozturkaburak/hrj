package com.ab.hr.service.mapper;

import static com.ab.hr.domain.AnswerAsserts.*;
import static com.ab.hr.domain.AnswerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnswerMapperTest {

    private AnswerMapper answerMapper;

    @BeforeEach
    void setUp() {
        answerMapper = new AnswerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAnswerSample1();
        var actual = answerMapper.toEntity(answerMapper.toDto(expected));
        assertAnswerAllPropertiesEquals(expected, actual);
    }
}
