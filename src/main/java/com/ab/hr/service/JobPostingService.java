package com.ab.hr.service;

import com.ab.hr.repository.JobPostingRepository;
import com.ab.hr.service.dto.JobPostingDTO;
import com.ab.hr.service.mapper.JobPostingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.JobPosting}.
 */
@Service
@Transactional
public class JobPostingService {

    private static final Logger log = LoggerFactory.getLogger(JobPostingService.class);

    private final JobPostingRepository jobPostingRepository;

    private final JobPostingMapper jobPostingMapper;

    public JobPostingService(JobPostingRepository jobPostingRepository, JobPostingMapper jobPostingMapper) {
        this.jobPostingRepository = jobPostingRepository;
        this.jobPostingMapper = jobPostingMapper;
    }

    /**
     * Save a jobPosting.
     *
     * @param jobPostingDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<JobPostingDTO> save(JobPostingDTO jobPostingDTO) {
        log.debug("Request to save JobPosting : {}", jobPostingDTO);
        return jobPostingRepository.save(jobPostingMapper.toEntity(jobPostingDTO)).map(jobPostingMapper::toDto);
    }

    /**
     * Update a jobPosting.
     *
     * @param jobPostingDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<JobPostingDTO> update(JobPostingDTO jobPostingDTO) {
        log.debug("Request to update JobPosting : {}", jobPostingDTO);
        return jobPostingRepository.save(jobPostingMapper.toEntity(jobPostingDTO)).map(jobPostingMapper::toDto);
    }

    /**
     * Partially update a jobPosting.
     *
     * @param jobPostingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<JobPostingDTO> partialUpdate(JobPostingDTO jobPostingDTO) {
        log.debug("Request to partially update JobPosting : {}", jobPostingDTO);

        return jobPostingRepository
            .findById(jobPostingDTO.getId())
            .map(existingJobPosting -> {
                jobPostingMapper.partialUpdate(existingJobPosting, jobPostingDTO);

                return existingJobPosting;
            })
            .flatMap(jobPostingRepository::save)
            .map(jobPostingMapper::toDto);
    }

    /**
     * Get all the jobPostings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<JobPostingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobPostings");
        return jobPostingRepository.findAllBy(pageable).map(jobPostingMapper::toDto);
    }

    /**
     * Returns the number of jobPostings available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return jobPostingRepository.count();
    }

    /**
     * Get one jobPosting by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<JobPostingDTO> findOne(Long id) {
        log.debug("Request to get JobPosting : {}", id);
        return jobPostingRepository.findById(id).map(jobPostingMapper::toDto);
    }

    /**
     * Delete the jobPosting by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete JobPosting : {}", id);
        return jobPostingRepository.deleteById(id);
    }
}
