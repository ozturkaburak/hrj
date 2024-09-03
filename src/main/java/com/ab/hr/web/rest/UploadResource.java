package com.ab.hr.web.rest;

import com.ab.hr.repository.UploadRepository;
import com.ab.hr.service.UploadService;
import com.ab.hr.service.dto.UploadDTO;
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
 * REST controller for managing {@link com.ab.hr.domain.Upload}.
 */
@RestController
@RequestMapping("/api/uploads")
public class UploadResource {

    private static final Logger log = LoggerFactory.getLogger(UploadResource.class);

    private static final String ENTITY_NAME = "upload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UploadService uploadService;

    private final UploadRepository uploadRepository;

    public UploadResource(UploadService uploadService, UploadRepository uploadRepository) {
        this.uploadService = uploadService;
        this.uploadRepository = uploadRepository;
    }

    /**
     * {@code POST  /uploads} : Create a new upload.
     *
     * @param uploadDTO the uploadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uploadDTO, or with status {@code 400 (Bad Request)} if the upload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UploadDTO>> createUpload(@Valid @RequestBody UploadDTO uploadDTO) throws URISyntaxException {
        log.debug("REST request to save Upload : {}", uploadDTO);
        if (uploadDTO.getId() != null) {
            throw new BadRequestAlertException("A new upload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return uploadService
            .save(uploadDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/uploads/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /uploads/:id} : Updates an existing upload.
     *
     * @param id the id of the uploadDTO to save.
     * @param uploadDTO the uploadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadDTO,
     * or with status {@code 400 (Bad Request)} if the uploadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UploadDTO>> updateUpload(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UploadDTO uploadDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Upload : {}, {}", id, uploadDTO);
        if (uploadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uploadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uploadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return uploadService
                    .update(uploadDTO)
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
     * {@code PATCH  /uploads/:id} : Partial updates given fields of an existing upload, field will ignore if it is null
     *
     * @param id the id of the uploadDTO to save.
     * @param uploadDTO the uploadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadDTO,
     * or with status {@code 400 (Bad Request)} if the uploadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uploadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uploadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UploadDTO>> partialUpdateUpload(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UploadDTO uploadDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Upload partially : {}, {}", id, uploadDTO);
        if (uploadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uploadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uploadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UploadDTO> result = uploadService.partialUpdate(uploadDTO);

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
     * {@code GET  /uploads} : get all the uploads.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uploads in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UploadDTO>>> getAllUploads(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Uploads");
        return uploadService
            .countAll()
            .zipWith(uploadService.findAll(pageable).collectList())
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
     * {@code GET  /uploads/:id} : get the "id" upload.
     *
     * @param id the id of the uploadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uploadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UploadDTO>> getUpload(@PathVariable("id") Long id) {
        log.debug("REST request to get Upload : {}", id);
        Mono<UploadDTO> uploadDTO = uploadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uploadDTO);
    }

    /**
     * {@code DELETE  /uploads/:id} : delete the "id" upload.
     *
     * @param id the id of the uploadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUpload(@PathVariable("id") Long id) {
        log.debug("REST request to delete Upload : {}", id);
        return uploadService
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
