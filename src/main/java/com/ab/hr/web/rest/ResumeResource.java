package com.ab.hr.web.rest;

import com.ab.hr.repository.ResumeRepository;
import com.ab.hr.service.ResumeService;
import com.ab.hr.service.dto.ResumeDTO;
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
 * REST controller for managing {@link com.ab.hr.domain.Resume}.
 */
@RestController
@RequestMapping("/api/resumes")
public class ResumeResource {

    private static final Logger log = LoggerFactory.getLogger(ResumeResource.class);

    private static final String ENTITY_NAME = "resume";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResumeService resumeService;

    private final ResumeRepository resumeRepository;

    public ResumeResource(ResumeService resumeService, ResumeRepository resumeRepository) {
        this.resumeService = resumeService;
        this.resumeRepository = resumeRepository;
    }

    /**
     * {@code POST  /resumes} : Create a new resume.
     *
     * @param resumeDTO the resumeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resumeDTO, or with status {@code 400 (Bad Request)} if the resume has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ResumeDTO>> createResume(@Valid @RequestBody ResumeDTO resumeDTO) throws URISyntaxException {
        log.debug("REST request to save Resume : {}", resumeDTO);
        if (resumeDTO.getId() != null) {
            throw new BadRequestAlertException("A new resume cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return resumeService
            .save(resumeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/resumes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /resumes/:id} : Updates an existing resume.
     *
     * @param id the id of the resumeDTO to save.
     * @param resumeDTO the resumeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resumeDTO,
     * or with status {@code 400 (Bad Request)} if the resumeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resumeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResumeDTO>> updateResume(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResumeDTO resumeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Resume : {}, {}", id, resumeDTO);
        if (resumeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resumeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return resumeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return resumeService
                    .update(resumeDTO)
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
     * {@code PATCH  /resumes/:id} : Partial updates given fields of an existing resume, field will ignore if it is null
     *
     * @param id the id of the resumeDTO to save.
     * @param resumeDTO the resumeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resumeDTO,
     * or with status {@code 400 (Bad Request)} if the resumeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resumeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resumeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ResumeDTO>> partialUpdateResume(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResumeDTO resumeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Resume partially : {}, {}", id, resumeDTO);
        if (resumeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resumeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return resumeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ResumeDTO> result = resumeService.partialUpdate(resumeDTO);

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
     * {@code GET  /resumes} : get all the resumes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resumes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ResumeDTO>>> getAllResumes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Resumes");
        return resumeService
            .countAll()
            .zipWith(resumeService.findAll(pageable).collectList())
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
     * {@code GET  /resumes/:id} : get the "id" resume.
     *
     * @param id the id of the resumeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resumeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResumeDTO>> getResume(@PathVariable("id") Long id) {
        log.debug("REST request to get Resume : {}", id);
        Mono<ResumeDTO> resumeDTO = resumeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resumeDTO);
    }

    /**
     * {@code DELETE  /resumes/:id} : delete the "id" resume.
     *
     * @param id the id of the resumeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteResume(@PathVariable("id") Long id) {
        log.debug("REST request to delete Resume : {}", id);
        return resumeService
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
