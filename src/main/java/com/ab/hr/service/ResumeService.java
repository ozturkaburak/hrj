package com.ab.hr.service;

import com.ab.hr.repository.ResumeRepository;
import com.ab.hr.service.dto.ResumeDTO;
import com.ab.hr.service.mapper.ResumeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.Resume}.
 */
@Service
@Transactional
public class ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final ResumeRepository resumeRepository;

    private final ResumeMapper resumeMapper;

    public ResumeService(ResumeRepository resumeRepository, ResumeMapper resumeMapper) {
        this.resumeRepository = resumeRepository;
        this.resumeMapper = resumeMapper;
    }

    /**
     * Save a resume.
     *
     * @param resumeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ResumeDTO> save(ResumeDTO resumeDTO) {
        log.debug("Request to save Resume : {}", resumeDTO);
        return resumeRepository.save(resumeMapper.toEntity(resumeDTO)).map(resumeMapper::toDto);
    }

    /**
     * Update a resume.
     *
     * @param resumeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ResumeDTO> update(ResumeDTO resumeDTO) {
        log.debug("Request to update Resume : {}", resumeDTO);
        return resumeRepository.save(resumeMapper.toEntity(resumeDTO)).map(resumeMapper::toDto);
    }

    /**
     * Partially update a resume.
     *
     * @param resumeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ResumeDTO> partialUpdate(ResumeDTO resumeDTO) {
        log.debug("Request to partially update Resume : {}", resumeDTO);

        return resumeRepository
            .findById(resumeDTO.getId())
            .map(existingResume -> {
                resumeMapper.partialUpdate(existingResume, resumeDTO);

                return existingResume;
            })
            .flatMap(resumeRepository::save)
            .map(resumeMapper::toDto);
    }

    /**
     * Get all the resumes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ResumeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Resumes");
        return resumeRepository.findAllBy(pageable).map(resumeMapper::toDto);
    }

    /**
     * Returns the number of resumes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return resumeRepository.count();
    }

    /**
     * Get one resume by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ResumeDTO> findOne(Long id) {
        log.debug("Request to get Resume : {}", id);
        return resumeRepository.findById(id).map(resumeMapper::toDto);
    }

    /**
     * Delete the resume by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Resume : {}", id);
        return resumeRepository.deleteById(id);
    }
}
