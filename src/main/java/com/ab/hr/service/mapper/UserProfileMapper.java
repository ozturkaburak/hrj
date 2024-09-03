package com.ab.hr.service.mapper;

import com.ab.hr.domain.User;
import com.ab.hr.domain.UserProfile;
import com.ab.hr.service.dto.UserDTO;
import com.ab.hr.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    UserProfileDTO toDto(UserProfile s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
