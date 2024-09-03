package com.ab.hr.domain;

import static com.ab.hr.domain.UploadTestSamples.*;
import static com.ab.hr.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UploadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Upload.class);
        Upload upload1 = getUploadSample1();
        Upload upload2 = new Upload();
        assertThat(upload1).isNotEqualTo(upload2);

        upload2.setId(upload1.getId());
        assertThat(upload1).isEqualTo(upload2);

        upload2 = getUploadSample2();
        assertThat(upload1).isNotEqualTo(upload2);
    }

    @Test
    void userProfileTest() {
        Upload upload = getUploadRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        upload.setUserProfile(userProfileBack);
        assertThat(upload.getUserProfile()).isEqualTo(userProfileBack);

        upload.userProfile(null);
        assertThat(upload.getUserProfile()).isNull();
    }
}
