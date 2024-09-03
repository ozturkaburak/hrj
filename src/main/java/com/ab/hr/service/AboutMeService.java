package com.ab.hr.service;

import com.ab.hr.repository.AboutMeRepository;
import com.ab.hr.service.dto.AboutMeDTO;
import com.ab.hr.service.mapper.AboutMeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.AboutMe}.
 */
@Service
@Transactional
public class AboutMeService {

    private static final Logger log = LoggerFactory.getLogger(AboutMeService.class);

    private final AboutMeRepository aboutMeRepository;

    private final AboutMeMapper aboutMeMapper;

    public AboutMeService(AboutMeRepository aboutMeRepository, AboutMeMapper aboutMeMapper) {
        this.aboutMeRepository = aboutMeRepository;
        this.aboutMeMapper = aboutMeMapper;
    }

    /**
     * Save a aboutMe.
     *
     * @param aboutMeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AboutMeDTO> save(AboutMeDTO aboutMeDTO) {
        log.debug("Request to save AboutMe : {}", aboutMeDTO);
        return aboutMeRepository.save(aboutMeMapper.toEntity(aboutMeDTO)).map(aboutMeMapper::toDto);
    }

    /**
     * Update a aboutMe.
     *
     * @param aboutMeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AboutMeDTO> update(AboutMeDTO aboutMeDTO) {
        log.debug("Request to update AboutMe : {}", aboutMeDTO);
        return aboutMeRepository.save(aboutMeMapper.toEntity(aboutMeDTO)).map(aboutMeMapper::toDto);
    }

    /**
     * Partially update a aboutMe.
     *
     * @param aboutMeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AboutMeDTO> partialUpdate(AboutMeDTO aboutMeDTO) {
        log.debug("Request to partially update AboutMe : {}", aboutMeDTO);

        return aboutMeRepository
            .findById(aboutMeDTO.getId())
            .map(existingAboutMe -> {
                aboutMeMapper.partialUpdate(existingAboutMe, aboutMeDTO);

                return existingAboutMe;
            })
            .flatMap(aboutMeRepository::save)
            .map(aboutMeMapper::toDto);
    }

    /**
     * Get all the aboutMes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AboutMeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AboutMes");
        return aboutMeRepository.findAllBy(pageable).map(aboutMeMapper::toDto);
    }

    /**
     * Returns the number of aboutMes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return aboutMeRepository.count();
    }

    /**
     * Get one aboutMe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AboutMeDTO> findOne(Long id) {
        log.debug("Request to get AboutMe : {}", id);
        return aboutMeRepository.findById(id).map(aboutMeMapper::toDto);
    }

    /**
     * Delete the aboutMe by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AboutMe : {}", id);
        return aboutMeRepository.deleteById(id);
    }
}
