package com.ab.hr.web.rest;

import com.ab.hr.repository.JobPostingRepository;
import com.ab.hr.service.JobPostingService;
import com.ab.hr.service.dto.JobPostingDTO;
import com.ab.hr.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.ab.hr.domain.JobPosting}.
 */
@RestController
@RequestMapping("/api/job-postings")
public class JobPostingResource {

    private static final Logger log = LoggerFactory.getLogger(JobPostingResource.class);

    private static final String ENTITY_NAME = "jobPosting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobPostingService jobPostingService;

    private final JobPostingRepository jobPostingRepository;

    public JobPostingResource(JobPostingService jobPostingService, JobPostingRepository jobPostingRepository) {
        this.jobPostingService = jobPostingService;
        this.jobPostingRepository = jobPostingRepository;
    }

    /**
     * {@code POST  /job-postings} : Create a new jobPosting.
     *
     * @param jobPostingDTO the jobPostingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobPostingDTO, or with status {@code 400 (Bad Request)} if the jobPosting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<JobPostingDTO>> createJobPosting(@Valid @RequestBody JobPostingDTO jobPostingDTO) throws URISyntaxException {
        log.debug("REST request to save JobPosting : {}", jobPostingDTO);
        if (jobPostingDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobPosting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return jobPostingService
            .save(jobPostingDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/job-postings/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /job-postings/:id} : Updates an existing jobPosting.
     *
     * @param id the id of the jobPostingDTO to save.
     * @param jobPostingDTO the jobPostingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobPostingDTO,
     * or with status {@code 400 (Bad Request)} if the jobPostingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobPostingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<JobPostingDTO>> updateJobPosting(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JobPostingDTO jobPostingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update JobPosting : {}, {}", id, jobPostingDTO);
        if (jobPostingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobPostingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return jobPostingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return jobPostingService
                    .update(jobPostingDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /job-postings/:id} : Partial updates given fields of an existing jobPosting, field will ignore if it is null
     *
     * @param id the id of the jobPostingDTO to save.
     * @param jobPostingDTO the jobPostingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobPostingDTO,
     * or with status {@code 400 (Bad Request)} if the jobPostingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jobPostingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobPostingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<JobPostingDTO>> partialUpdateJobPosting(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JobPostingDTO jobPostingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobPosting partially : {}, {}", id, jobPostingDTO);
        if (jobPostingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobPostingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return jobPostingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<JobPostingDTO> result = jobPostingService.partialUpdate(jobPostingDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /job-postings} : get all the jobPostings.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobPostings in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<JobPostingDTO>>> getAllJobPostings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of JobPostings");
        return jobPostingService
            .countAll()
            .zipWith(jobPostingService.findAll(pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /job-postings/:id} : get the "id" jobPosting.
     *
     * @param id the id of the jobPostingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobPostingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<JobPostingDTO>> getJobPosting(@PathVariable("id") Long id) {
        log.debug("REST request to get JobPosting : {}", id);
        Mono<JobPostingDTO> jobPostingDTO = jobPostingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobPostingDTO);
    }

    /**
     * {@code DELETE  /job-postings/:id} : delete the "id" jobPosting.
     *
     * @param id the id of the jobPostingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteJobPosting(@PathVariable("id") Long id) {
        log.debug("REST request to delete JobPosting : {}", id);
        return jobPostingService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
