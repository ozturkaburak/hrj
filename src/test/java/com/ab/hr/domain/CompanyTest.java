package com.ab.hr.domain;

import static com.ab.hr.domain.CityTestSamples.*;
import static com.ab.hr.domain.CompanyTestSamples.*;
import static com.ab.hr.domain.ExperienceTestSamples.*;
import static com.ab.hr.domain.JobPostingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = getCompanySample1();
        Company company2 = new Company();
        assertThat(company1).isNotEqualTo(company2);

        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);

        company2 = getCompanySample2();
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    void cityTest() {
        Company company = getCompanyRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        company.setCity(cityBack);
        assertThat(company.getCity()).isEqualTo(cityBack);

        company.city(null);
        assertThat(company.getCity()).isNull();
    }

    @Test
    void jobPostingTest() {
        Company company = getCompanyRandomSampleGenerator();
        JobPosting jobPostingBack = getJobPostingRandomSampleGenerator();

        company.setJobPosting(jobPostingBack);
        assertThat(company.getJobPosting()).isEqualTo(jobPostingBack);
        assertThat(jobPostingBack.getCompany()).isEqualTo(company);

        company.jobPosting(null);
        assertThat(company.getJobPosting()).isNull();
        assertThat(jobPostingBack.getCompany()).isNull();
    }

    @Test
    void experienceTest() {
        Company company = getCompanyRandomSampleGenerator();
        Experience experienceBack = getExperienceRandomSampleGenerator();

        company.setExperience(experienceBack);
        assertThat(company.getExperience()).isEqualTo(experienceBack);
        assertThat(experienceBack.getCompany()).isEqualTo(company);

        company.experience(null);
        assertThat(company.getExperience()).isNull();
        assertThat(experienceBack.getCompany()).isNull();
    }
}
