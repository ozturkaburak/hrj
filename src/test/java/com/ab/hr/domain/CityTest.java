package com.ab.hr.domain;

import static com.ab.hr.domain.CityTestSamples.*;
import static com.ab.hr.domain.CompanyTestSamples.*;
import static com.ab.hr.domain.ContactTestSamples.*;
import static com.ab.hr.domain.CountryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(City.class);
        City city1 = getCitySample1();
        City city2 = new City();
        assertThat(city1).isNotEqualTo(city2);

        city2.setId(city1.getId());
        assertThat(city1).isEqualTo(city2);

        city2 = getCitySample2();
        assertThat(city1).isNotEqualTo(city2);
    }

    @Test
    void countryTest() {
        City city = getCityRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        city.setCountry(countryBack);
        assertThat(city.getCountry()).isEqualTo(countryBack);

        city.country(null);
        assertThat(city.getCountry()).isNull();
    }

    @Test
    void contactTest() {
        City city = getCityRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        city.setContact(contactBack);
        assertThat(city.getContact()).isEqualTo(contactBack);
        assertThat(contactBack.getCity()).isEqualTo(city);

        city.contact(null);
        assertThat(city.getContact()).isNull();
        assertThat(contactBack.getCity()).isNull();
    }

    @Test
    void companyTest() {
        City city = getCityRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        city.setCompany(companyBack);
        assertThat(city.getCompany()).isEqualTo(companyBack);
        assertThat(companyBack.getCity()).isEqualTo(city);

        city.company(null);
        assertThat(city.getCompany()).isNull();
        assertThat(companyBack.getCity()).isNull();
    }
}
