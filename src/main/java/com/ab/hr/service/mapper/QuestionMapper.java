package com.ab.hr.service.mapper;

import com.ab.hr.domain.Assignment;
import com.ab.hr.domain.Question;
import com.ab.hr.service.dto.AssignmentDTO;
import com.ab.hr.service.dto.QuestionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "assignments", source = "assignments", qualifiedByName = "assignmentIdSet")
    QuestionDTO toDto(Question s);

    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "removeAssignment", ignore = true)
    Question toEntity(QuestionDTO questionDTO);

    @Named("assignmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssignmentDTO toDtoAssignmentId(Assignment assignment);

    @Named("assignmentIdSet")
    default Set<AssignmentDTO> toDtoAssignmentIdSet(Set<Assignment> assignment) {
        return assignment.stream().map(this::toDtoAssignmentId).collect(Collectors.toSet());
    }
}
