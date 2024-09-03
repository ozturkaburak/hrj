package com.ab.hr.repository;

import com.ab.hr.domain.Certificate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Certificate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CertificateRepository extends ReactiveCrudRepository<Certificate, Long>, CertificateRepositoryInternal {
    Flux<Certificate> findAllBy(Pageable pageable);

    @Query("SELECT * FROM certificate entity WHERE entity.user_profile_id = :id")
    Flux<Certificate> findByUserProfile(Long id);

    @Query("SELECT * FROM certificate entity WHERE entity.user_profile_id IS NULL")
    Flux<Certificate> findAllWhereUserProfileIsNull();

    @Override
    <S extends Certificate> Mono<S> save(S entity);

    @Override
    Flux<Certificate> findAll();

    @Override
    Mono<Certificate> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CertificateRepositoryInternal {
    <S extends Certificate> Mono<S> save(S entity);

    Flux<Certificate> findAllBy(Pageable pageable);

    Flux<Certificate> findAll();

    Mono<Certificate> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Certificate> findAllBy(Pageable pageable, Criteria criteria);
}
