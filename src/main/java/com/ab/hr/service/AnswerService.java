package com.ab.hr.service;

import com.ab.hr.repository.AnswerRepository;
import com.ab.hr.service.dto.AnswerDTO;
import com.ab.hr.service.mapper.AnswerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.Answer}.
 */
@Service
@Transactional
public class AnswerService {

    private static final Logger log = LoggerFactory.getLogger(AnswerService.class);

    private final AnswerRepository answerRepository;

    private final AnswerMapper answerMapper;

    public AnswerService(AnswerRepository answerRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
    }

    /**
     * Save a answer.
     *
     * @param answerDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AnswerDTO> save(AnswerDTO answerDTO) {
        log.debug("Request to save Answer : {}", answerDTO);
        return answerRepository.save(answerMapper.toEntity(answerDTO)).map(answerMapper::toDto);
    }

    /**
     * Update a answer.
     *
     * @param answerDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AnswerDTO> update(AnswerDTO answerDTO) {
        log.debug("Request to update Answer : {}", answerDTO);
        return answerRepository.save(answerMapper.toEntity(answerDTO)).map(answerMapper::toDto);
    }

    /**
     * Partially update a answer.
     *
     * @param answerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AnswerDTO> partialUpdate(AnswerDTO answerDTO) {
        log.debug("Request to partially update Answer : {}", answerDTO);

        return answerRepository
            .findById(answerDTO.getId())
            .map(existingAnswer -> {
                answerMapper.partialUpdate(existingAnswer, answerDTO);

                return existingAnswer;
            })
            .flatMap(answerRepository::save)
            .map(answerMapper::toDto);
    }

    /**
     * Get all the answers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Answers");
        return answerRepository.findAllBy(pageable).map(answerMapper::toDto);
    }

    /**
     * Returns the number of answers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return answerRepository.count();
    }

    /**
     * Get one answer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AnswerDTO> findOne(Long id) {
        log.debug("Request to get Answer : {}", id);
        return answerRepository.findById(id).map(answerMapper::toDto);
    }

    /**
     * Delete the answer by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Answer : {}", id);
        return answerRepository.deleteById(id);
    }
}
