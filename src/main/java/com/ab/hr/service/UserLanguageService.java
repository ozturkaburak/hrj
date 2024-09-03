package com.ab.hr.service;

import com.ab.hr.repository.UserLanguageRepository;
import com.ab.hr.service.dto.UserLanguageDTO;
import com.ab.hr.service.mapper.UserLanguageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.UserLanguage}.
 */
@Service
@Transactional
public class UserLanguageService {

    private static final Logger log = LoggerFactory.getLogger(UserLanguageService.class);

    private final UserLanguageRepository userLanguageRepository;

    private final UserLanguageMapper userLanguageMapper;

    public UserLanguageService(UserLanguageRepository userLanguageRepository, UserLanguageMapper userLanguageMapper) {
        this.userLanguageRepository = userLanguageRepository;
        this.userLanguageMapper = userLanguageMapper;
    }

    /**
     * Save a userLanguage.
     *
     * @param userLanguageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserLanguageDTO> save(UserLanguageDTO userLanguageDTO) {
        log.debug("Request to save UserLanguage : {}", userLanguageDTO);
        return userLanguageRepository.save(userLanguageMapper.toEntity(userLanguageDTO)).map(userLanguageMapper::toDto);
    }

    /**
     * Update a userLanguage.
     *
     * @param userLanguageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserLanguageDTO> update(UserLanguageDTO userLanguageDTO) {
        log.debug("Request to update UserLanguage : {}", userLanguageDTO);
        return userLanguageRepository.save(userLanguageMapper.toEntity(userLanguageDTO)).map(userLanguageMapper::toDto);
    }

    /**
     * Partially update a userLanguage.
     *
     * @param userLanguageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UserLanguageDTO> partialUpdate(UserLanguageDTO userLanguageDTO) {
        log.debug("Request to partially update UserLanguage : {}", userLanguageDTO);

        return userLanguageRepository
            .findById(userLanguageDTO.getId())
            .map(existingUserLanguage -> {
                userLanguageMapper.partialUpdate(existingUserLanguage, userLanguageDTO);

                return existingUserLanguage;
            })
            .flatMap(userLanguageRepository::save)
            .map(userLanguageMapper::toDto);
    }

    /**
     * Get all the userLanguages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserLanguageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserLanguages");
        return userLanguageRepository.findAllBy(pageable).map(userLanguageMapper::toDto);
    }

    /**
     * Returns the number of userLanguages available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return userLanguageRepository.count();
    }

    /**
     * Get one userLanguage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UserLanguageDTO> findOne(Long id) {
        log.debug("Request to get UserLanguage : {}", id);
        return userLanguageRepository.findById(id).map(userLanguageMapper::toDto);
    }

    /**
     * Delete the userLanguage by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserLanguage : {}", id);
        return userLanguageRepository.deleteById(id);
    }
}
