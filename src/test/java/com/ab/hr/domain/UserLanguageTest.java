package com.ab.hr.domain;

import static com.ab.hr.domain.LanguageTestSamples.*;
import static com.ab.hr.domain.UserLanguageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserLanguageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserLanguage.class);
        UserLanguage userLanguage1 = getUserLanguageSample1();
        UserLanguage userLanguage2 = new UserLanguage();
        assertThat(userLanguage1).isNotEqualTo(userLanguage2);

        userLanguage2.setId(userLanguage1.getId());
        assertThat(userLanguage1).isEqualTo(userLanguage2);

        userLanguage2 = getUserLanguageSample2();
        assertThat(userLanguage1).isNotEqualTo(userLanguage2);
    }

    @Test
    void languageTest() {
        UserLanguage userLanguage = getUserLanguageRandomSampleGenerator();
        Language languageBack = getLanguageRandomSampleGenerator();

        userLanguage.setLanguage(languageBack);
        assertThat(userLanguage.getLanguage()).isEqualTo(languageBack);

        userLanguage.language(null);
        assertThat(userLanguage.getLanguage()).isNull();
    }
}
