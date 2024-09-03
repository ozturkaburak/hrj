package com.ab.hr.service.mapper;

import com.ab.hr.domain.Language;
import com.ab.hr.domain.User;
import com.ab.hr.domain.UserLanguage;
import com.ab.hr.service.dto.LanguageDTO;
import com.ab.hr.service.dto.UserDTO;
import com.ab.hr.service.dto.UserLanguageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserLanguage} and its DTO {@link UserLanguageDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserLanguageMapper extends EntityMapper<UserLanguageDTO, UserLanguage> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "language", source = "language", qualifiedByName = "languageId")
    UserLanguageDTO toDto(UserLanguage s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("languageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LanguageDTO toDtoLanguageId(Language language);
}
