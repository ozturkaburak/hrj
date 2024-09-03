package com.ab.hr.domain;

import static com.ab.hr.domain.AboutMeTestSamples.*;
import static com.ab.hr.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AboutMeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AboutMe.class);
        AboutMe aboutMe1 = getAboutMeSample1();
        AboutMe aboutMe2 = new AboutMe();
        assertThat(aboutMe1).isNotEqualTo(aboutMe2);

        aboutMe2.setId(aboutMe1.getId());
        assertThat(aboutMe1).isEqualTo(aboutMe2);

        aboutMe2 = getAboutMeSample2();
        assertThat(aboutMe1).isNotEqualTo(aboutMe2);
    }

    @Test
    void userProfileTest() {
        AboutMe aboutMe = getAboutMeRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        aboutMe.setUserProfile(userProfileBack);
        assertThat(aboutMe.getUserProfile()).isEqualTo(userProfileBack);

        aboutMe.userProfile(null);
        assertThat(aboutMe.getUserProfile()).isNull();
    }
}
