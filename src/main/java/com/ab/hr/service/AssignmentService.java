package com.ab.hr.service;

import com.ab.hr.repository.AssignmentRepository;
import com.ab.hr.service.dto.AssignmentDTO;
import com.ab.hr.service.mapper.AssignmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.Assignment}.
 */
@Service
@Transactional
public class AssignmentService {

    private static final Logger log = LoggerFactory.getLogger(AssignmentService.class);

    private final AssignmentRepository assignmentRepository;

    private final AssignmentMapper assignmentMapper;

    public AssignmentService(AssignmentRepository assignmentRepository, AssignmentMapper assignmentMapper) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
    }

    /**
     * Save a assignment.
     *
     * @param assignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AssignmentDTO> save(AssignmentDTO assignmentDTO) {
        log.debug("Request to save Assignment : {}", assignmentDTO);
        return assignmentRepository.save(assignmentMapper.toEntity(assignmentDTO)).map(assignmentMapper::toDto);
    }

    /**
     * Update a assignment.
     *
     * @param assignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AssignmentDTO> update(AssignmentDTO assignmentDTO) {
        log.debug("Request to update Assignment : {}", assignmentDTO);
        return assignmentRepository.save(assignmentMapper.toEntity(assignmentDTO)).map(assignmentMapper::toDto);
    }

    /**
     * Partially update a assignment.
     *
     * @param assignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AssignmentDTO> partialUpdate(AssignmentDTO assignmentDTO) {
        log.debug("Request to partially update Assignment : {}", assignmentDTO);

        return assignmentRepository
            .findById(assignmentDTO.getId())
            .map(existingAssignment -> {
                assignmentMapper.partialUpdate(existingAssignment, assignmentDTO);

                return existingAssignment;
            })
            .flatMap(assignmentRepository::save)
            .map(assignmentMapper::toDto);
    }

    /**
     * Get all the assignments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AssignmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Assignments");
        return assignmentRepository.findAllBy(pageable).map(assignmentMapper::toDto);
    }

    /**
     * Get all the assignments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<AssignmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assignmentRepository.findAllWithEagerRelationships(pageable).map(assignmentMapper::toDto);
    }

    /**
     *  Get all the assignments where UserAssignment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AssignmentDTO> findAllWhereUserAssignmentIsNull() {
        log.debug("Request to get all assignments where UserAssignment is null");
        return assignmentRepository.findAllWhereUserAssignmentIsNull().map(assignmentMapper::toDto);
    }

    /**
     * Returns the number of assignments available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return assignmentRepository.count();
    }

    /**
     * Get one assignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AssignmentDTO> findOne(Long id) {
        log.debug("Request to get Assignment : {}", id);
        return assignmentRepository.findOneWithEagerRelationships(id).map(assignmentMapper::toDto);
    }

    /**
     * Delete the assignment by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Assignment : {}", id);
        return assignmentRepository.deleteById(id);
    }
}
