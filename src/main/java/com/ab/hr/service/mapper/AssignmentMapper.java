package com.ab.hr.service.mapper;

import com.ab.hr.domain.Assignment;
import com.ab.hr.domain.Question;
import com.ab.hr.service.dto.AssignmentDTO;
import com.ab.hr.service.dto.QuestionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Assignment} and its DTO {@link AssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssignmentMapper extends EntityMapper<AssignmentDTO, Assignment> {
    @Mapping(target = "questions", source = "questions", qualifiedByName = "questionIdSet")
    AssignmentDTO toDto(Assignment s);

    @Mapping(target = "removeQuestions", ignore = true)
    Assignment toEntity(AssignmentDTO assignmentDTO);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);

    @Named("questionIdSet")
    default Set<QuestionDTO> toDtoQuestionIdSet(Set<Question> question) {
        return question.stream().map(this::toDtoQuestionId).collect(Collectors.toSet());
    }
}
