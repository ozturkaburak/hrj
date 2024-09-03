package com.ab.hr.service.mapper;

import com.ab.hr.domain.Answer;
import com.ab.hr.domain.Question;
import com.ab.hr.domain.User;
import com.ab.hr.service.dto.AnswerDTO;
import com.ab.hr.service.dto.QuestionDTO;
import com.ab.hr.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Answer} and its DTO {@link AnswerDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {
    @Mapping(target = "question", source = "question", qualifiedByName = "questionId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    AnswerDTO toDto(Answer s);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
