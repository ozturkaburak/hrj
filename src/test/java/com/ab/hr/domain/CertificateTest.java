package com.ab.hr.domain;

import static com.ab.hr.domain.CertificateTestSamples.*;
import static com.ab.hr.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CertificateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Certificate.class);
        Certificate certificate1 = getCertificateSample1();
        Certificate certificate2 = new Certificate();
        assertThat(certificate1).isNotEqualTo(certificate2);

        certificate2.setId(certificate1.getId());
        assertThat(certificate1).isEqualTo(certificate2);

        certificate2 = getCertificateSample2();
        assertThat(certificate1).isNotEqualTo(certificate2);
    }

    @Test
    void userProfileTest() {
        Certificate certificate = getCertificateRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        certificate.setUserProfile(userProfileBack);
        assertThat(certificate.getUserProfile()).isEqualTo(userProfileBack);

        certificate.userProfile(null);
        assertThat(certificate.getUserProfile()).isNull();
    }
}
