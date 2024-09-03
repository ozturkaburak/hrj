package com.ab.hr.web.rest;

import com.ab.hr.repository.AboutMeRepository;
import com.ab.hr.service.AboutMeService;
import com.ab.hr.service.dto.AboutMeDTO;
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
 * REST controller for managing {@link com.ab.hr.domain.AboutMe}.
 */
@RestController
@RequestMapping("/api/about-mes")
public class AboutMeResource {

    private static final Logger log = LoggerFactory.getLogger(AboutMeResource.class);

    private static final String ENTITY_NAME = "aboutMe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AboutMeService aboutMeService;

    private final AboutMeRepository aboutMeRepository;

    public AboutMeResource(AboutMeService aboutMeService, AboutMeRepository aboutMeRepository) {
        this.aboutMeService = aboutMeService;
        this.aboutMeRepository = aboutMeRepository;
    }

    /**
     * {@code POST  /about-mes} : Create a new aboutMe.
     *
     * @param aboutMeDTO the aboutMeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aboutMeDTO, or with status {@code 400 (Bad Request)} if the aboutMe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AboutMeDTO>> createAboutMe(@Valid @RequestBody AboutMeDTO aboutMeDTO) throws URISyntaxException {
        log.debug("REST request to save AboutMe : {}", aboutMeDTO);
        if (aboutMeDTO.getId() != null) {
            throw new BadRequestAlertException("A new aboutMe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return aboutMeService
            .save(aboutMeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/about-mes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /about-mes/:id} : Updates an existing aboutMe.
     *
     * @param id the id of the aboutMeDTO to save.
     * @param aboutMeDTO the aboutMeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutMeDTO,
     * or with status {@code 400 (Bad Request)} if the aboutMeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aboutMeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AboutMeDTO>> updateAboutMe(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AboutMeDTO aboutMeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AboutMe : {}, {}", id, aboutMeDTO);
        if (aboutMeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutMeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aboutMeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return aboutMeService
                    .update(aboutMeDTO)
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
     * {@code PATCH  /about-mes/:id} : Partial updates given fields of an existing aboutMe, field will ignore if it is null
     *
     * @param id the id of the aboutMeDTO to save.
     * @param aboutMeDTO the aboutMeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutMeDTO,
     * or with status {@code 400 (Bad Request)} if the aboutMeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aboutMeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aboutMeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AboutMeDTO>> partialUpdateAboutMe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AboutMeDTO aboutMeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AboutMe partially : {}, {}", id, aboutMeDTO);
        if (aboutMeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutMeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aboutMeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AboutMeDTO> result = aboutMeService.partialUpdate(aboutMeDTO);

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
     * {@code GET  /about-mes} : get all the aboutMes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aboutMes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AboutMeDTO>>> getAllAboutMes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AboutMes");
        return aboutMeService
            .countAll()
            .zipWith(aboutMeService.findAll(pageable).collectList())
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
     * {@code GET  /about-mes/:id} : get the "id" aboutMe.
     *
     * @param id the id of the aboutMeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aboutMeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AboutMeDTO>> getAboutMe(@PathVariable("id") Long id) {
        log.debug("REST request to get AboutMe : {}", id);
        Mono<AboutMeDTO> aboutMeDTO = aboutMeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aboutMeDTO);
    }

    /**
     * {@code DELETE  /about-mes/:id} : delete the "id" aboutMe.
     *
     * @param id the id of the aboutMeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAboutMe(@PathVariable("id") Long id) {
        log.debug("REST request to delete AboutMe : {}", id);
        return aboutMeService
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
