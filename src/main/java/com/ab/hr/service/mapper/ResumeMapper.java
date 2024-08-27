package com.ab.hr.service.mapper;

import com.ab.hr.domain.Resume;
import com.ab.hr.domain.User;
import com.ab.hr.service.dto.ResumeDTO;
import com.ab.hr.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resume} and its DTO {@link ResumeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResumeMapper extends EntityMapper<ResumeDTO, Resume> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ResumeDTO toDto(Resume s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
