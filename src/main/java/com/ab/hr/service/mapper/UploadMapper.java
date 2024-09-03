package com.ab.hr.service.mapper;

import com.ab.hr.domain.Upload;
import com.ab.hr.domain.UserProfile;
import com.ab.hr.service.dto.UploadDTO;
import com.ab.hr.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Upload} and its DTO {@link UploadDTO}.
 */
@Mapper(componentModel = "spring")
public interface UploadMapper extends EntityMapper<UploadDTO, Upload> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    UploadDTO toDto(Upload s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
