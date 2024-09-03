package com.ab.hr.service.mapper;

import static com.ab.hr.domain.CertificateAsserts.*;
import static com.ab.hr.domain.CertificateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CertificateMapperTest {

    private CertificateMapper certificateMapper;

    @BeforeEach
    void setUp() {
        certificateMapper = new CertificateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCertificateSample1();
        var actual = certificateMapper.toEntity(certificateMapper.toDto(expected));
        assertCertificateAllPropertiesEquals(expected, actual);
    }
}
