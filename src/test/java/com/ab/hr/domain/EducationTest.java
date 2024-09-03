package com.ab.hr.domain;

import static com.ab.hr.domain.EducationTestSamples.*;
import static com.ab.hr.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EducationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Education.class);
        Education education1 = getEducationSample1();
        Education education2 = new Education();
        assertThat(education1).isNotEqualTo(education2);

        education2.setId(education1.getId());
        assertThat(education1).isEqualTo(education2);

        education2 = getEducationSample2();
        assertThat(education1).isNotEqualTo(education2);
    }

    @Test
    void userProfileTest() {
        Education education = getEducationRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        education.setUserProfile(userProfileBack);
        assertThat(education.getUserProfile()).isEqualTo(userProfileBack);

        education.userProfile(null);
        assertThat(education.getUserProfile()).isNull();
    }
}
