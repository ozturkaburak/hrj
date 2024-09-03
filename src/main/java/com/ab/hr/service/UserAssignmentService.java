package com.ab.hr.service;

import com.ab.hr.repository.UserAssignmentRepository;
import com.ab.hr.service.dto.UserAssignmentDTO;
import com.ab.hr.service.mapper.UserAssignmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.UserAssignment}.
 */
@Service
@Transactional
public class UserAssignmentService {

    private static final Logger log = LoggerFactory.getLogger(UserAssignmentService.class);

    private final UserAssignmentRepository userAssignmentRepository;

    private final UserAssignmentMapper userAssignmentMapper;

    public UserAssignmentService(UserAssignmentRepository userAssignmentRepository, UserAssignmentMapper userAssignmentMapper) {
        this.userAssignmentRepository = userAssignmentRepository;
        this.userAssignmentMapper = userAssignmentMapper;
    }

    /**
     * Save a userAssignment.
     *
     * @param userAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserAssignmentDTO> save(UserAssignmentDTO userAssignmentDTO) {
        log.debug("Request to save UserAssignment : {}", userAssignmentDTO);
        return userAssignmentRepository.save(userAssignmentMapper.toEntity(userAssignmentDTO)).map(userAssignmentMapper::toDto);
    }

    /**
     * Update a userAssignment.
     *
     * @param userAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserAssignmentDTO> update(UserAssignmentDTO userAssignmentDTO) {
        log.debug("Request to update UserAssignment : {}", userAssignmentDTO);
        return userAssignmentRepository.save(userAssignmentMapper.toEntity(userAssignmentDTO)).map(userAssignmentMapper::toDto);
    }

    /**
     * Partially update a userAssignment.
     *
     * @param userAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UserAssignmentDTO> partialUpdate(UserAssignmentDTO userAssignmentDTO) {
        log.debug("Request to partially update UserAssignment : {}", userAssignmentDTO);

        return userAssignmentRepository
            .findById(userAssignmentDTO.getId())
            .map(existingUserAssignment -> {
                userAssignmentMapper.partialUpdate(existingUserAssignment, userAssignmentDTO);

                return existingUserAssignment;
            })
            .flatMap(userAssignmentRepository::save)
            .map(userAssignmentMapper::toDto);
    }

    /**
     * Get all the userAssignments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserAssignmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAssignments");
        return userAssignmentRepository.findAllBy(pageable).map(userAssignmentMapper::toDto);
    }

    /**
     * Returns the number of userAssignments available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return userAssignmentRepository.count();
    }

    /**
     * Get one userAssignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UserAssignmentDTO> findOne(Long id) {
        log.debug("Request to get UserAssignment : {}", id);
        return userAssignmentRepository.findById(id).map(userAssignmentMapper::toDto);
    }

    /**
     * Delete the userAssignment by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserAssignment : {}", id);
        return userAssignmentRepository.deleteById(id);
    }
}
