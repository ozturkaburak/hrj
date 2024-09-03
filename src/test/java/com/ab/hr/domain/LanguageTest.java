package com.ab.hr.domain;

import static com.ab.hr.domain.LanguageTestSamples.*;
import static com.ab.hr.domain.UserLanguageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LanguageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Language.class);
        Language language1 = getLanguageSample1();
        Language language2 = new Language();
        assertThat(language1).isNotEqualTo(language2);

        language2.setId(language1.getId());
        assertThat(language1).isEqualTo(language2);

        language2 = getLanguageSample2();
        assertThat(language1).isNotEqualTo(language2);
    }

    @Test
    void userLanguageTest() {
        Language language = getLanguageRandomSampleGenerator();
        UserLanguage userLanguageBack = getUserLanguageRandomSampleGenerator();

        language.setUserLanguage(userLanguageBack);
        assertThat(language.getUserLanguage()).isEqualTo(userLanguageBack);
        assertThat(userLanguageBack.getLanguage()).isEqualTo(language);

        language.userLanguage(null);
        assertThat(language.getUserLanguage()).isNull();
        assertThat(userLanguageBack.getLanguage()).isNull();
    }
}
