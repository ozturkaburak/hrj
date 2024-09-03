package com.ab.hr.service.mapper;

import static com.ab.hr.domain.ContactAsserts.*;
import static com.ab.hr.domain.ContactTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactMapperTest {

    private ContactMapper contactMapper;

    @BeforeEach
    void setUp() {
        contactMapper = new ContactMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContactSample1();
        var actual = contactMapper.toEntity(contactMapper.toDto(expected));
        assertContactAllPropertiesEquals(expected, actual);
    }
}
