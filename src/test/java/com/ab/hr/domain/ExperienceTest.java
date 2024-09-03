package com.ab.hr.domain;

import static com.ab.hr.domain.ExperienceTestSamples.*;
import static com.ab.hr.domain.SkillTestSamples.*;
import static com.ab.hr.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExperienceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Experience.class);
        Experience experience1 = getExperienceSample1();
        Experience experience2 = new Experience();
        assertThat(experience1).isNotEqualTo(experience2);

        experience2.setId(experience1.getId());
        assertThat(experience1).isEqualTo(experience2);

        experience2 = getExperienceSample2();
        assertThat(experience1).isNotEqualTo(experience2);
    }

    @Test
    void skillsTest() {
        Experience experience = getExperienceRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        experience.addSkills(skillBack);
        assertThat(experience.getSkills()).containsOnly(skillBack);
        assertThat(skillBack.getExperience()).isEqualTo(experience);

        experience.removeSkills(skillBack);
        assertThat(experience.getSkills()).doesNotContain(skillBack);
        assertThat(skillBack.getExperience()).isNull();

        experience.skills(new HashSet<>(Set.of(skillBack)));
        assertThat(experience.getSkills()).containsOnly(skillBack);
        assertThat(skillBack.getExperience()).isEqualTo(experience);

        experience.setSkills(new HashSet<>());
        assertThat(experience.getSkills()).doesNotContain(skillBack);
        assertThat(skillBack.getExperience()).isNull();
    }

    @Test
    void userProfileTest() {
        Experience experience = getExperienceRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        experience.setUserProfile(userProfileBack);
        assertThat(experience.getUserProfile()).isEqualTo(userProfileBack);

        experience.userProfile(null);
        assertThat(experience.getUserProfile()).isNull();
    }
}
