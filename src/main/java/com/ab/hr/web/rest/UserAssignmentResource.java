package com.ab.hr.web.rest;

import com.ab.hr.repository.UserAssignmentRepository;
import com.ab.hr.service.UserAssignmentService;
import com.ab.hr.service.dto.UserAssignmentDTO;
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
 * REST controller for managing {@link com.ab.hr.domain.UserAssignment}.
 */
@RestController
@RequestMapping("/api/user-assignments")
public class UserAssignmentResource {

    private static final Logger log = LoggerFactory.getLogger(UserAssignmentResource.class);

    private static final String ENTITY_NAME = "userAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAssignmentService userAssignmentService;

    private final UserAssignmentRepository userAssignmentRepository;

    public UserAssignmentResource(UserAssignmentService userAssignmentService, UserAssignmentRepository userAssignmentRepository) {
        this.userAssignmentService = userAssignmentService;
        this.userAssignmentRepository = userAssignmentRepository;
    }

    /**
     * {@code POST  /user-assignments} : Create a new userAssignment.
     *
     * @param userAssignmentDTO the userAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAssignmentDTO, or with status {@code 400 (Bad Request)} if the userAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserAssignmentDTO>> createUserAssignment(@Valid @RequestBody UserAssignmentDTO userAssignmentDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserAssignment : {}", userAssignmentDTO);
        if (userAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userAssignmentService
            .save(userAssignmentDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/user-assignments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-assignments/:id} : Updates an existing userAssignment.
     *
     * @param id the id of the userAssignmentDTO to save.
     * @param userAssignmentDTO the userAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the userAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserAssignmentDTO>> updateUserAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAssignmentDTO userAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAssignment : {}, {}", id, userAssignmentDTO);
        if (userAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userAssignmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userAssignmentService
                    .update(userAssignmentDTO)
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
     * {@code PATCH  /user-assignments/:id} : Partial updates given fields of an existing userAssignment, field will ignore if it is null
     *
     * @param id the id of the userAssignmentDTO to save.
     * @param userAssignmentDTO the userAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the userAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserAssignmentDTO>> partialUpdateUserAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAssignmentDTO userAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAssignment partially : {}, {}", id, userAssignmentDTO);
        if (userAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userAssignmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserAssignmentDTO> result = userAssignmentService.partialUpdate(userAssignmentDTO);

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
     * {@code GET  /user-assignments} : get all the userAssignments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAssignments in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserAssignmentDTO>>> getAllUserAssignments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserAssignments");
        return userAssignmentService
            .countAll()
            .zipWith(userAssignmentService.findAll(pageable).collectList())
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
     * {@code GET  /user-assignments/:id} : get the "id" userAssignment.
     *
     * @param id the id of the userAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserAssignmentDTO>> getUserAssignment(@PathVariable("id") Long id) {
        log.debug("REST request to get UserAssignment : {}", id);
        Mono<UserAssignmentDTO> userAssignmentDTO = userAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAssignmentDTO);
    }

    /**
     * {@code DELETE  /user-assignments/:id} : delete the "id" userAssignment.
     *
     * @param id the id of the userAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserAssignment(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserAssignment : {}", id);
        return userAssignmentService
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
