package com.ab.hr.repository;

import com.ab.hr.domain.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Country entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryRepository extends ReactiveCrudRepository<Country, Long>, CountryRepositoryInternal {
    Flux<Country> findAllBy(Pageable pageable);

    @Override
    <S extends Country> Mono<S> save(S entity);

    @Override
    Flux<Country> findAll();

    @Override
    Mono<Country> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CountryRepositoryInternal {
    <S extends Country> Mono<S> save(S entity);

    Flux<Country> findAllBy(Pageable pageable);

    Flux<Country> findAll();

    Mono<Country> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Country> findAllBy(Pageable pageable, Criteria criteria);
}
