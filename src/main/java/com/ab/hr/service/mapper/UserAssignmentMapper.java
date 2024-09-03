package com.ab.hr.service.mapper;

import com.ab.hr.domain.Assignment;
import com.ab.hr.domain.User;
import com.ab.hr.domain.UserAssignment;
import com.ab.hr.service.dto.AssignmentDTO;
import com.ab.hr.service.dto.UserAssignmentDTO;
import com.ab.hr.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAssignment} and its DTO {@link UserAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAssignmentMapper extends EntityMapper<UserAssignmentDTO, UserAssignment> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "assignment", source = "assignment", qualifiedByName = "assignmentId")
    UserAssignmentDTO toDto(UserAssignment s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("assignmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssignmentDTO toDtoAssignmentId(Assignment assignment);
}
