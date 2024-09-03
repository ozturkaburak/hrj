package com.ab.hr.domain;

import static com.ab.hr.domain.AboutMeTestSamples.*;
import static com.ab.hr.domain.CertificateTestSamples.*;
import static com.ab.hr.domain.EducationTestSamples.*;
import static com.ab.hr.domain.ExperienceTestSamples.*;
import static com.ab.hr.domain.UploadTestSamples.*;
import static com.ab.hr.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void experiencesTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Experience experienceBack = getExperienceRandomSampleGenerator();

        userProfile.addExperiences(experienceBack);
        assertThat(userProfile.getExperiences()).containsOnly(experienceBack);
        assertThat(experienceBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeExperiences(experienceBack);
        assertThat(userProfile.getExperiences()).doesNotContain(experienceBack);
        assertThat(experienceBack.getUserProfile()).isNull();

        userProfile.experiences(new HashSet<>(Set.of(experienceBack)));
        assertThat(userProfile.getExperiences()).containsOnly(experienceBack);
        assertThat(experienceBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setExperiences(new HashSet<>());
        assertThat(userProfile.getExperiences()).doesNotContain(experienceBack);
        assertThat(experienceBack.getUserProfile()).isNull();
    }

    @Test
    void educationsTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Education educationBack = getEducationRandomSampleGenerator();

        userProfile.addEducations(educationBack);
        assertThat(userProfile.getEducations()).containsOnly(educationBack);
        assertThat(educationBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeEducations(educationBack);
        assertThat(userProfile.getEducations()).doesNotContain(educationBack);
        assertThat(educationBack.getUserProfile()).isNull();

        userProfile.educations(new HashSet<>(Set.of(educationBack)));
        assertThat(userProfile.getEducations()).containsOnly(educationBack);
        assertThat(educationBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setEducations(new HashSet<>());
        assertThat(userProfile.getEducations()).doesNotContain(educationBack);
        assertThat(educationBack.getUserProfile()).isNull();
    }

    @Test
    void certificatesTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Certificate certificateBack = getCertificateRandomSampleGenerator();

        userProfile.addCertificates(certificateBack);
        assertThat(userProfile.getCertificates()).containsOnly(certificateBack);
        assertThat(certificateBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeCertificates(certificateBack);
        assertThat(userProfile.getCertificates()).doesNotContain(certificateBack);
        assertThat(certificateBack.getUserProfile()).isNull();

        userProfile.certificates(new HashSet<>(Set.of(certificateBack)));
        assertThat(userProfile.getCertificates()).containsOnly(certificateBack);
        assertThat(certificateBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setCertificates(new HashSet<>());
        assertThat(userProfile.getCertificates()).doesNotContain(certificateBack);
        assertThat(certificateBack.getUserProfile()).isNull();
    }

    @Test
    void aboutMeTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        AboutMe aboutMeBack = getAboutMeRandomSampleGenerator();

        userProfile.addAboutMe(aboutMeBack);
        assertThat(userProfile.getAboutMes()).containsOnly(aboutMeBack);
        assertThat(aboutMeBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeAboutMe(aboutMeBack);
        assertThat(userProfile.getAboutMes()).doesNotContain(aboutMeBack);
        assertThat(aboutMeBack.getUserProfile()).isNull();

        userProfile.aboutMes(new HashSet<>(Set.of(aboutMeBack)));
        assertThat(userProfile.getAboutMes()).containsOnly(aboutMeBack);
        assertThat(aboutMeBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setAboutMes(new HashSet<>());
        assertThat(userProfile.getAboutMes()).doesNotContain(aboutMeBack);
        assertThat(aboutMeBack.getUserProfile()).isNull();
    }

    @Test
    void uploadsTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Upload uploadBack = getUploadRandomSampleGenerator();

        userProfile.addUploads(uploadBack);
        assertThat(userProfile.getUploads()).containsOnly(uploadBack);
        assertThat(uploadBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeUploads(uploadBack);
        assertThat(userProfile.getUploads()).doesNotContain(uploadBack);
        assertThat(uploadBack.getUserProfile()).isNull();

        userProfile.uploads(new HashSet<>(Set.of(uploadBack)));
        assertThat(userProfile.getUploads()).containsOnly(uploadBack);
        assertThat(uploadBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setUploads(new HashSet<>());
        assertThat(userProfile.getUploads()).doesNotContain(uploadBack);
        assertThat(uploadBack.getUserProfile()).isNull();
    }
}
