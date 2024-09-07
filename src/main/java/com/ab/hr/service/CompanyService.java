package com.ab.hr.service;

import com.ab.hr.repository.CompanyRepository;
import com.ab.hr.service.dto.CompanyDTO;
import com.ab.hr.service.mapper.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.Company}.
 */
@Service
@Transactional
public class CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        return companyRepository.save(companyMapper.toEntity(companyDTO)).map(companyMapper::toDto);
    }

    /**
     * Update a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> update(CompanyDTO companyDTO) {
        log.debug("Request to update Company : {}", companyDTO);
        return companyRepository.save(companyMapper.toEntity(companyDTO)).map(companyMapper::toDto);
    }

    /**
     * Partially update a company.
     *
     * @param companyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> partialUpdate(CompanyDTO companyDTO) {
        log.debug("Request to partially update Company : {}", companyDTO);

        return companyRepository
            .findById(companyDTO.getId())
            .map(existingCompany -> {
                companyMapper.partialUpdate(existingCompany, companyDTO);

                return existingCompany;
            })
            .flatMap(companyRepository::save)
            .map(companyMapper::toDto);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CompanyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAllBy(pageable).map(companyMapper::toDto);
    }

    /**
     *  Get all the companies where JobPosting is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CompanyDTO> findAllWhereJobPostingIsNull() {
        log.debug("Request to get all companies where JobPosting is null");
        return companyRepository.findAllWhereJobPostingIsNull().map(companyMapper::toDto);
    }

    /**
     *  Get all the companies where Experience is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CompanyDTO> findAllWhereExperienceIsNull() {
        log.debug("Request to get all companies where Experience is null");
        return companyRepository.findAllWhereExperienceIsNull().map(companyMapper::toDto);
    }

    /**
     * Returns the number of companies available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return companyRepository.count();
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CompanyDTO> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id).map(companyMapper::toDto);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        return companyRepository.deleteById(id);
    }
}
