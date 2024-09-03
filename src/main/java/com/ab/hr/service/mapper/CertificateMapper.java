package com.ab.hr.service.mapper;

import com.ab.hr.domain.Certificate;
import com.ab.hr.domain.UserProfile;
import com.ab.hr.service.dto.CertificateDTO;
import com.ab.hr.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Certificate} and its DTO {@link CertificateDTO}.
 */
@Mapper(componentModel = "spring")
public interface CertificateMapper extends EntityMapper<CertificateDTO, Certificate> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    CertificateDTO toDto(Certificate s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
