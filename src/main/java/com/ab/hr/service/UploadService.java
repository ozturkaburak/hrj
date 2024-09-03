package com.ab.hr.service;

import com.ab.hr.repository.UploadRepository;
import com.ab.hr.service.dto.UploadDTO;
import com.ab.hr.service.mapper.UploadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.Upload}.
 */
@Service
@Transactional
public class UploadService {

    private static final Logger log = LoggerFactory.getLogger(UploadService.class);

    private final UploadRepository uploadRepository;

    private final UploadMapper uploadMapper;

    public UploadService(UploadRepository uploadRepository, UploadMapper uploadMapper) {
        this.uploadRepository = uploadRepository;
        this.uploadMapper = uploadMapper;
    }

    /**
     * Save a upload.
     *
     * @param uploadDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UploadDTO> save(UploadDTO uploadDTO) {
        log.debug("Request to save Upload : {}", uploadDTO);
        return uploadRepository.save(uploadMapper.toEntity(uploadDTO)).map(uploadMapper::toDto);
    }

    /**
     * Update a upload.
     *
     * @param uploadDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UploadDTO> update(UploadDTO uploadDTO) {
        log.debug("Request to update Upload : {}", uploadDTO);
        return uploadRepository.save(uploadMapper.toEntity(uploadDTO)).map(uploadMapper::toDto);
    }

    /**
     * Partially update a upload.
     *
     * @param uploadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UploadDTO> partialUpdate(UploadDTO uploadDTO) {
        log.debug("Request to partially update Upload : {}", uploadDTO);

        return uploadRepository
            .findById(uploadDTO.getId())
            .map(existingUpload -> {
                uploadMapper.partialUpdate(existingUpload, uploadDTO);

                return existingUpload;
            })
            .flatMap(uploadRepository::save)
            .map(uploadMapper::toDto);
    }

    /**
     * Get all the uploads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UploadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Uploads");
        return uploadRepository.findAllBy(pageable).map(uploadMapper::toDto);
    }

    /**
     * Returns the number of uploads available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return uploadRepository.count();
    }

    /**
     * Get one upload by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UploadDTO> findOne(Long id) {
        log.debug("Request to get Upload : {}", id);
        return uploadRepository.findById(id).map(uploadMapper::toDto);
    }

    /**
     * Delete the upload by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Upload : {}", id);
        return uploadRepository.deleteById(id);
    }
}
