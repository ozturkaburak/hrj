package com.ab.hr.web.rest;

import com.ab.hr.repository.UserLanguageRepository;
import com.ab.hr.service.UserLanguageService;
import com.ab.hr.service.dto.UserLanguageDTO;
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
 * REST controller for managing {@link com.ab.hr.domain.UserLanguage}.
 */
@RestController
@RequestMapping("/api/user-languages")
public class UserLanguageResource {

    private static final Logger log = LoggerFactory.getLogger(UserLanguageResource.class);

    private static final String ENTITY_NAME = "userLanguage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserLanguageService userLanguageService;

    private final UserLanguageRepository userLanguageRepository;

    public UserLanguageResource(UserLanguageService userLanguageService, UserLanguageRepository userLanguageRepository) {
        this.userLanguageService = userLanguageService;
        this.userLanguageRepository = userLanguageRepository;
    }

    /**
     * {@code POST  /user-languages} : Create a new userLanguage.
     *
     * @param userLanguageDTO the userLanguageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userLanguageDTO, or with status {@code 400 (Bad Request)} if the userLanguage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserLanguageDTO>> createUserLanguage(@Valid @RequestBody UserLanguageDTO userLanguageDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserLanguage : {}", userLanguageDTO);
        if (userLanguageDTO.getId() != null) {
            throw new BadRequestAlertException("A new userLanguage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userLanguageService
            .save(userLanguageDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/user-languages/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-languages/:id} : Updates an existing userLanguage.
     *
     * @param id the id of the userLanguageDTO to save.
     * @param userLanguageDTO the userLanguageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userLanguageDTO,
     * or with status {@code 400 (Bad Request)} if the userLanguageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userLanguageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserLanguageDTO>> updateUserLanguage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserLanguageDTO userLanguageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserLanguage : {}, {}", id, userLanguageDTO);
        if (userLanguageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userLanguageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userLanguageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userLanguageService
                    .update(userLanguageDTO)
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
     * {@code PATCH  /user-languages/:id} : Partial updates given fields of an existing userLanguage, field will ignore if it is null
     *
     * @param id the id of the userLanguageDTO to save.
     * @param userLanguageDTO the userLanguageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userLanguageDTO,
     * or with status {@code 400 (Bad Request)} if the userLanguageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userLanguageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userLanguageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserLanguageDTO>> partialUpdateUserLanguage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserLanguageDTO userLanguageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserLanguage partially : {}, {}", id, userLanguageDTO);
        if (userLanguageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userLanguageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userLanguageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserLanguageDTO> result = userLanguageService.partialUpdate(userLanguageDTO);

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
     * {@code GET  /user-languages} : get all the userLanguages.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userLanguages in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserLanguageDTO>>> getAllUserLanguages(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserLanguages");
        return userLanguageService
            .countAll()
            .zipWith(userLanguageService.findAll(pageable).collectList())
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
     * {@code GET  /user-languages/:id} : get the "id" userLanguage.
     *
     * @param id the id of the userLanguageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userLanguageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserLanguageDTO>> getUserLanguage(@PathVariable("id") Long id) {
        log.debug("REST request to get UserLanguage : {}", id);
        Mono<UserLanguageDTO> userLanguageDTO = userLanguageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userLanguageDTO);
    }

    /**
     * {@code DELETE  /user-languages/:id} : delete the "id" userLanguage.
     *
     * @param id the id of the userLanguageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserLanguage(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserLanguage : {}", id);
        return userLanguageService
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
