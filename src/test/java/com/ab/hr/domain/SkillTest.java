package com.ab.hr.domain;

import static com.ab.hr.domain.ExperienceTestSamples.*;
import static com.ab.hr.domain.SkillTestSamples.*;
import static com.ab.hr.domain.UserSkillTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Skill.class);
        Skill skill1 = getSkillSample1();
        Skill skill2 = new Skill();
        assertThat(skill1).isNotEqualTo(skill2);

        skill2.setId(skill1.getId());
        assertThat(skill1).isEqualTo(skill2);

        skill2 = getSkillSample2();
        assertThat(skill1).isNotEqualTo(skill2);
    }

    @Test
    void experienceTest() {
        Skill skill = getSkillRandomSampleGenerator();
        Experience experienceBack = getExperienceRandomSampleGenerator();

        skill.setExperience(experienceBack);
        assertThat(skill.getExperience()).isEqualTo(experienceBack);

        skill.experience(null);
        assertThat(skill.getExperience()).isNull();
    }

    @Test
    void userSkillTest() {
        Skill skill = getSkillRandomSampleGenerator();
        UserSkill userSkillBack = getUserSkillRandomSampleGenerator();

        skill.setUserSkill(userSkillBack);
        assertThat(skill.getUserSkill()).isEqualTo(userSkillBack);
        assertThat(userSkillBack.getSkill()).isEqualTo(skill);

        skill.userSkill(null);
        assertThat(skill.getUserSkill()).isNull();
        assertThat(userSkillBack.getSkill()).isNull();
    }
}
