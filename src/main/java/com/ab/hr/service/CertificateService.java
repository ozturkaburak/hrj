package com.ab.hr.service;

import com.ab.hr.repository.CertificateRepository;
import com.ab.hr.service.dto.CertificateDTO;
import com.ab.hr.service.mapper.CertificateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.ab.hr.domain.Certificate}.
 */
@Service
@Transactional
public class CertificateService {

    private static final Logger log = LoggerFactory.getLogger(CertificateService.class);

    private final CertificateRepository certificateRepository;

    private final CertificateMapper certificateMapper;

    public CertificateService(CertificateRepository certificateRepository, CertificateMapper certificateMapper) {
        this.certificateRepository = certificateRepository;
        this.certificateMapper = certificateMapper;
    }

    /**
     * Save a certificate.
     *
     * @param certificateDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CertificateDTO> save(CertificateDTO certificateDTO) {
        log.debug("Request to save Certificate : {}", certificateDTO);
        return certificateRepository.save(certificateMapper.toEntity(certificateDTO)).map(certificateMapper::toDto);
    }

    /**
     * Update a certificate.
     *
     * @param certificateDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CertificateDTO> update(CertificateDTO certificateDTO) {
        log.debug("Request to update Certificate : {}", certificateDTO);
        return certificateRepository.save(certificateMapper.toEntity(certificateDTO)).map(certificateMapper::toDto);
    }

    /**
     * Partially update a certificate.
     *
     * @param certificateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CertificateDTO> partialUpdate(CertificateDTO certificateDTO) {
        log.debug("Request to partially update Certificate : {}", certificateDTO);

        return certificateRepository
            .findById(certificateDTO.getId())
            .map(existingCertificate -> {
                certificateMapper.partialUpdate(existingCertificate, certificateDTO);

                return existingCertificate;
            })
            .flatMap(certificateRepository::save)
            .map(certificateMapper::toDto);
    }

    /**
     * Get all the certificates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CertificateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Certificates");
        return certificateRepository.findAllBy(pageable).map(certificateMapper::toDto);
    }

    /**
     * Returns the number of certificates available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return certificateRepository.count();
    }

    /**
     * Get one certificate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CertificateDTO> findOne(Long id) {
        log.debug("Request to get Certificate : {}", id);
        return certificateRepository.findById(id).map(certificateMapper::toDto);
    }

    /**
     * Delete the certificate by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Certificate : {}", id);
        return certificateRepository.deleteById(id);
    }
}
