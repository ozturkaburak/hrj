package com.ab.hr.service;

import com.ab.hr.repository.EducationRepository;
import com.ab.hr.service.dto.EducationDTO;
import com.ab.hr.service.mapper.EducationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.Education}.
 */
@Service
@Transactional
public class EducationService {

    private static final Logger log = LoggerFactory.getLogger(EducationService.class);

    private final EducationRepository educationRepository;

    private final EducationMapper educationMapper;

    public EducationService(EducationRepository educationRepository, EducationMapper educationMapper) {
        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
    }

    /**
     * Save a education.
     *
     * @param educationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EducationDTO> save(EducationDTO educationDTO) {
        log.debug("Request to save Education : {}", educationDTO);
        return educationRepository.save(educationMapper.toEntity(educationDTO)).map(educationMapper::toDto);
    }

    /**
     * Update a education.
     *
     * @param educationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EducationDTO> update(EducationDTO educationDTO) {
        log.debug("Request to update Education : {}", educationDTO);
        return educationRepository.save(educationMapper.toEntity(educationDTO)).map(educationMapper::toDto);
    }

    /**
     * Partially update a education.
     *
     * @param educationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<EducationDTO> partialUpdate(EducationDTO educationDTO) {
        log.debug("Request to partially update Education : {}", educationDTO);

        return educationRepository
            .findById(educationDTO.getId())
            .map(existingEducation -> {
                educationMapper.partialUpdate(existingEducation, educationDTO);

                return existingEducation;
            })
            .flatMap(educationRepository::save)
            .map(educationMapper::toDto);
    }

    /**
     * Get all the educations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EducationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Educations");
        return educationRepository.findAllBy(pageable).map(educationMapper::toDto);
    }

    /**
     * Returns the number of educations available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return educationRepository.count();
    }

    /**
     * Get one education by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<EducationDTO> findOne(Long id) {
        log.debug("Request to get Education : {}", id);
        return educationRepository.findById(id).map(educationMapper::toDto);
    }

    /**
     * Delete the education by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Education : {}", id);
        return educationRepository.deleteById(id);
    }
}
