package com.ab.hr.web.rest;

import com.ab.hr.repository.UserSkillRepository;
import com.ab.hr.service.UserSkillService;
import com.ab.hr.service.dto.UserSkillDTO;
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
 * REST controller for managing {@link com.ab.hr.domain.UserSkill}.
 */
@RestController
@RequestMapping("/api/user-skills")
public class UserSkillResource {

    private static final Logger log = LoggerFactory.getLogger(UserSkillResource.class);

    private static final String ENTITY_NAME = "userSkill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSkillService userSkillService;

    private final UserSkillRepository userSkillRepository;

    public UserSkillResource(UserSkillService userSkillService, UserSkillRepository userSkillRepository) {
        this.userSkillService = userSkillService;
        this.userSkillRepository = userSkillRepository;
    }

    /**
     * {@code POST  /user-skills} : Create a new userSkill.
     *
     * @param userSkillDTO the userSkillDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSkillDTO, or with status {@code 400 (Bad Request)} if the userSkill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserSkillDTO>> createUserSkill(@Valid @RequestBody UserSkillDTO userSkillDTO) throws URISyntaxException {
        log.debug("REST request to save UserSkill : {}", userSkillDTO);
        if (userSkillDTO.getId() != null) {
            throw new BadRequestAlertException("A new userSkill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userSkillService
            .save(userSkillDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/user-skills/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-skills/:id} : Updates an existing userSkill.
     *
     * @param id the id of the userSkillDTO to save.
     * @param userSkillDTO the userSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSkillDTO,
     * or with status {@code 400 (Bad Request)} if the userSkillDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserSkillDTO>> updateUserSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserSkillDTO userSkillDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserSkill : {}, {}", id, userSkillDTO);
        if (userSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userSkillRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userSkillService
                    .update(userSkillDTO)
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
     * {@code PATCH  /user-skills/:id} : Partial updates given fields of an existing userSkill, field will ignore if it is null
     *
     * @param id the id of the userSkillDTO to save.
     * @param userSkillDTO the userSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSkillDTO,
     * or with status {@code 400 (Bad Request)} if the userSkillDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userSkillDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserSkillDTO>> partialUpdateUserSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserSkillDTO userSkillDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserSkill partially : {}, {}", id, userSkillDTO);
        if (userSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userSkillRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserSkillDTO> result = userSkillService.partialUpdate(userSkillDTO);

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
     * {@code GET  /user-skills} : get all the userSkills.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSkills in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserSkillDTO>>> getAllUserSkills(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserSkills");
        return userSkillService
            .countAll()
            .zipWith(userSkillService.findAll(pageable).collectList())
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
     * {@code GET  /user-skills/:id} : get the "id" userSkill.
     *
     * @param id the id of the userSkillDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSkillDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserSkillDTO>> getUserSkill(@PathVariable("id") Long id) {
        log.debug("REST request to get UserSkill : {}", id);
        Mono<UserSkillDTO> userSkillDTO = userSkillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSkillDTO);
    }

    /**
     * {@code DELETE  /user-skills/:id} : delete the "id" userSkill.
     *
     * @param id the id of the userSkillDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserSkill(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserSkill : {}", id);
        return userSkillService
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
