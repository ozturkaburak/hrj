package com.ab.hr.service.mapper;

import com.ab.hr.domain.Company;
import com.ab.hr.domain.Experience;
import com.ab.hr.domain.UserProfile;
import com.ab.hr.service.dto.CompanyDTO;
import com.ab.hr.service.dto.ExperienceDTO;
import com.ab.hr.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Experience} and its DTO {@link ExperienceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExperienceMapper extends EntityMapper<ExperienceDTO, Experience> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    ExperienceDTO toDto(Experience s);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
