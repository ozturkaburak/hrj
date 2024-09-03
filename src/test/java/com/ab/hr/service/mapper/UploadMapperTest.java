package com.ab.hr.service.mapper;

import static com.ab.hr.domain.UploadAsserts.*;
import static com.ab.hr.domain.UploadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UploadMapperTest {

    private UploadMapper uploadMapper;

    @BeforeEach
    void setUp() {
        uploadMapper = new UploadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUploadSample1();
        var actual = uploadMapper.toEntity(uploadMapper.toDto(expected));
        assertUploadAllPropertiesEquals(expected, actual);
    }
}
