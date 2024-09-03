package com.ab.hr.service;

import com.ab.hr.repository.UserSkillRepository;
import com.ab.hr.service.dto.UserSkillDTO;
import com.ab.hr.service.mapper.UserSkillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.UserSkill}.
 */
@Service
@Transactional
public class UserSkillService {

    private static final Logger log = LoggerFactory.getLogger(UserSkillService.class);

    private final UserSkillRepository userSkillRepository;

    private final UserSkillMapper userSkillMapper;

    public UserSkillService(UserSkillRepository userSkillRepository, UserSkillMapper userSkillMapper) {
        this.userSkillRepository = userSkillRepository;
        this.userSkillMapper = userSkillMapper;
    }

    /**
     * Save a userSkill.
     *
     * @param userSkillDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserSkillDTO> save(UserSkillDTO userSkillDTO) {
        log.debug("Request to save UserSkill : {}", userSkillDTO);
        return userSkillRepository.save(userSkillMapper.toEntity(userSkillDTO)).map(userSkillMapper::toDto);
    }

    /**
     * Update a userSkill.
     *
     * @param userSkillDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserSkillDTO> update(UserSkillDTO userSkillDTO) {
        log.debug("Request to update UserSkill : {}", userSkillDTO);
        return userSkillRepository.save(userSkillMapper.toEntity(userSkillDTO)).map(userSkillMapper::toDto);
    }

    /**
     * Partially update a userSkill.
     *
     * @param userSkillDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UserSkillDTO> partialUpdate(UserSkillDTO userSkillDTO) {
        log.debug("Request to partially update UserSkill : {}", userSkillDTO);

        return userSkillRepository
            .findById(userSkillDTO.getId())
            .map(existingUserSkill -> {
                userSkillMapper.partialUpdate(existingUserSkill, userSkillDTO);

                return existingUserSkill;
            })
            .flatMap(userSkillRepository::save)
            .map(userSkillMapper::toDto);
    }

    /**
     * Get all the userSkills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserSkillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserSkills");
        return userSkillRepository.findAllBy(pageable).map(userSkillMapper::toDto);
    }

    /**
     * Returns the number of userSkills available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return userSkillRepository.count();
    }

    /**
     * Get one userSkill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UserSkillDTO> findOne(Long id) {
        log.debug("Request to get UserSkill : {}", id);
        return userSkillRepository.findById(id).map(userSkillMapper::toDto);
    }

    /**
     * Delete the userSkill by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserSkill : {}", id);
        return userSkillRepository.deleteById(id);
    }
}
