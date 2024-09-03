package com.ab.hr.service.mapper;

import com.ab.hr.domain.Education;
import com.ab.hr.domain.UserProfile;
import com.ab.hr.service.dto.EducationDTO;
import com.ab.hr.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Education} and its DTO {@link EducationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EducationMapper extends EntityMapper<EducationDTO, Education> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    EducationDTO toDto(Education s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
