package com.ab.hr.service.mapper;

import com.ab.hr.domain.Skill;
import com.ab.hr.domain.User;
import com.ab.hr.domain.UserSkill;
import com.ab.hr.service.dto.SkillDTO;
import com.ab.hr.service.dto.UserDTO;
import com.ab.hr.service.dto.UserSkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSkill} and its DTO {@link UserSkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSkillMapper extends EntityMapper<UserSkillDTO, UserSkill> {
    @Mapping(target = "skill", source = "skill", qualifiedByName = "skillId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    UserSkillDTO toDto(UserSkill s);

    @Named("skillId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SkillDTO toDtoSkillId(Skill skill);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
