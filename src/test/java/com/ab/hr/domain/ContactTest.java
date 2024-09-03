package com.ab.hr.domain;

import static com.ab.hr.domain.CityTestSamples.*;
import static com.ab.hr.domain.ContactTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contact.class);
        Contact contact1 = getContactSample1();
        Contact contact2 = new Contact();
        assertThat(contact1).isNotEqualTo(contact2);

        contact2.setId(contact1.getId());
        assertThat(contact1).isEqualTo(contact2);

        contact2 = getContactSample2();
        assertThat(contact1).isNotEqualTo(contact2);
    }

    @Test
    void cityTest() {
        Contact contact = getContactRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        contact.setCity(cityBack);
        assertThat(contact.getCity()).isEqualTo(cityBack);

        contact.city(null);
        assertThat(contact.getCity()).isNull();
    }
}
