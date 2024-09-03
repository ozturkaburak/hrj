package com.ab.hr.service;

import com.ab.hr.repository.ExperienceRepository;
import com.ab.hr.service.dto.ExperienceDTO;
import com.ab.hr.service.mapper.ExperienceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.Experience}.
 */
@Service
@Transactional
public class ExperienceService {

    private static final Logger log = LoggerFactory.getLogger(ExperienceService.class);

    private final ExperienceRepository experienceRepository;

    private final ExperienceMapper experienceMapper;

    public ExperienceService(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper) {
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
    }

    /**
     * Save a experience.
     *
     * @param experienceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ExperienceDTO> save(ExperienceDTO experienceDTO) {
        log.debug("Request to save Experience : {}", experienceDTO);
        return experienceRepository.save(experienceMapper.toEntity(experienceDTO)).map(experienceMapper::toDto);
    }

    /**
     * Update a experience.
     *
     * @param experienceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ExperienceDTO> update(ExperienceDTO experienceDTO) {
        log.debug("Request to update Experience : {}", experienceDTO);
        return experienceRepository.save(experienceMapper.toEntity(experienceDTO)).map(experienceMapper::toDto);
    }

    /**
     * Partially update a experience.
     *
     * @param experienceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ExperienceDTO> partialUpdate(ExperienceDTO experienceDTO) {
        log.debug("Request to partially update Experience : {}", experienceDTO);

        return experienceRepository
            .findById(experienceDTO.getId())
            .map(existingExperience -> {
                experienceMapper.partialUpdate(existingExperience, experienceDTO);

                return existingExperience;
            })
            .flatMap(experienceRepository::save)
            .map(experienceMapper::toDto);
    }

    /**
     * Get all the experiences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ExperienceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Experiences");
        return experienceRepository.findAllBy(pageable).map(experienceMapper::toDto);
    }

    /**
     * Returns the number of experiences available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return experienceRepository.count();
    }

    /**
     * Get one experience by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ExperienceDTO> findOne(Long id) {
        log.debug("Request to get Experience : {}", id);
        return experienceRepository.findById(id).map(experienceMapper::toDto);
    }

    /**
     * Delete the experience by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Experience : {}", id);
        return experienceRepository.deleteById(id);
    }
}
