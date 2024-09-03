package com.ab.hr.service.mapper;

import com.ab.hr.domain.Experience;
import com.ab.hr.domain.Skill;
import com.ab.hr.service.dto.ExperienceDTO;
import com.ab.hr.service.dto.SkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {
    @Mapping(target = "experience", source = "experience", qualifiedByName = "experienceId")
    SkillDTO toDto(Skill s);

    @Named("experienceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExperienceDTO toDtoExperienceId(Experience experience);
}
