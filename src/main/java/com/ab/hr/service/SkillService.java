package com.ab.hr.service;

import com.ab.hr.repository.SkillRepository;
import com.ab.hr.service.dto.SkillDTO;
import com.ab.hr.service.mapper.SkillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.Skill}.
 */
@Service
@Transactional
public class SkillService {

    private static final Logger log = LoggerFactory.getLogger(SkillService.class);

    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;

    public SkillService(SkillRepository skillRepository, SkillMapper skillMapper) {
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    /**
     * Save a skill.
     *
     * @param skillDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SkillDTO> save(SkillDTO skillDTO) {
        log.debug("Request to save Skill : {}", skillDTO);
        return skillRepository.save(skillMapper.toEntity(skillDTO)).map(skillMapper::toDto);
    }

    /**
     * Update a skill.
     *
     * @param skillDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SkillDTO> update(SkillDTO skillDTO) {
        log.debug("Request to update Skill : {}", skillDTO);
        return skillRepository.save(skillMapper.toEntity(skillDTO)).map(skillMapper::toDto);
    }

    /**
     * Partially update a skill.
     *
     * @param skillDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SkillDTO> partialUpdate(SkillDTO skillDTO) {
        log.debug("Request to partially update Skill : {}", skillDTO);

        return skillRepository
            .findById(skillDTO.getId())
            .map(existingSkill -> {
                skillMapper.partialUpdate(existingSkill, skillDTO);

                return existingSkill;
            })
            .flatMap(skillRepository::save)
            .map(skillMapper::toDto);
    }

    /**
     * Get all the skills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SkillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Skills");
        return skillRepository.findAllBy(pageable).map(skillMapper::toDto);
    }

    /**
     *  Get all the skills where UserSkill is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SkillDTO> findAllWhereUserSkillIsNull() {
        log.debug("Request to get all skills where UserSkill is null");
        return skillRepository.findAllWhereUserSkillIsNull().map(skillMapper::toDto);
    }

    /**
     * Returns the number of skills available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return skillRepository.count();
    }

    /**
     * Get one skill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SkillDTO> findOne(Long id) {
        log.debug("Request to get Skill : {}", id);
        return skillRepository.findById(id).map(skillMapper::toDto);
    }

    /**
     * Delete the skill by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Skill : {}", id);
        return skillRepository.deleteById(id);
    }
}
