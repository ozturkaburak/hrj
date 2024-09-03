package com.ab.hr.service.mapper;

import com.ab.hr.domain.AboutMe;
import com.ab.hr.domain.UserProfile;
import com.ab.hr.service.dto.AboutMeDTO;
import com.ab.hr.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AboutMe} and its DTO {@link AboutMeDTO}.
 */
@Mapper(componentModel = "spring")
public interface AboutMeMapper extends EntityMapper<AboutMeDTO, AboutMe> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    AboutMeDTO toDto(AboutMe s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
