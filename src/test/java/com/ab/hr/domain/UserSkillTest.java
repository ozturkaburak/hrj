package com.ab.hr.domain;

import static com.ab.hr.domain.SkillTestSamples.*;
import static com.ab.hr.domain.UserSkillTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ab.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSkill.class);
        UserSkill userSkill1 = getUserSkillSample1();
        UserSkill userSkill2 = new UserSkill();
        assertThat(userSkill1).isNotEqualTo(userSkill2);

        userSkill2.setId(userSkill1.getId());
        assertThat(userSkill1).isEqualTo(userSkill2);

        userSkill2 = getUserSkillSample2();
        assertThat(userSkill1).isNotEqualTo(userSkill2);
    }

    @Test
    void skillTest() {
        UserSkill userSkill = getUserSkillRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        userSkill.setSkill(skillBack);
        assertThat(userSkill.getSkill()).isEqualTo(skillBack);

        userSkill.skill(null);
        assertThat(userSkill.getSkill()).isNull();
    }
}
