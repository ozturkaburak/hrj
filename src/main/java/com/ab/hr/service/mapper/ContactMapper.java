package com.ab.hr.service.mapper;

import com.ab.hr.domain.City;
import com.ab.hr.domain.Contact;
import com.ab.hr.domain.User;
import com.ab.hr.service.dto.CityDTO;
import com.ab.hr.service.dto.ContactDTO;
import com.ab.hr.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "city", source = "city", qualifiedByName = "cityId")
    ContactDTO toDto(Contact s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("cityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CityDTO toDtoCityId(City city);
}
