package com.ab.hr.service;

import com.ab.hr.repository.QuestionRepository;
import com.ab.hr.service.dto.QuestionDTO;
import com.ab.hr.service.mapper.QuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.Question}.
 */
@Service
@Transactional
public class QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    public QuestionService(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuestionDTO> save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        return questionRepository.save(questionMapper.toEntity(questionDTO)).map(questionMapper::toDto);
    }

    /**
     * Update a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuestionDTO> update(QuestionDTO questionDTO) {
        log.debug("Request to update Question : {}", questionDTO);
        return questionRepository.save(questionMapper.toEntity(questionDTO)).map(questionMapper::toDto);
    }

    /**
     * Partially update a question.
     *
     * @param questionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<QuestionDTO> partialUpdate(QuestionDTO questionDTO) {
        log.debug("Request to partially update Question : {}", questionDTO);

        return questionRepository
            .findById(questionDTO.getId())
            .map(existingQuestion -> {
                questionMapper.partialUpdate(existingQuestion, questionDTO);

                return existingQuestion;
            })
            .flatMap(questionRepository::save)
            .map(questionMapper::toDto);
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<QuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAllBy(pageable).map(questionMapper::toDto);
    }

    /**
     *  Get all the questions where Answer is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<QuestionDTO> findAllWhereAnswerIsNull() {
        log.debug("Request to get all questions where Answer is null");
        return questionRepository.findAllWhereAnswerIsNull().map(questionMapper::toDto);
    }

    /**
     * Returns the number of questions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return questionRepository.count();
    }

    /**
     * Get one question by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<QuestionDTO> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findById(id).map(questionMapper::toDto);
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        return questionRepository.deleteById(id);
    }
}
