package com.ab.hr.repository;

import com.ab.hr.domain.Education;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Education entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationRepository extends ReactiveCrudRepository<Education, Long>, EducationRepositoryInternal {
    Flux<Education> findAllBy(Pageable pageable);

    @Query("SELECT * FROM education entity WHERE entity.user_profile_id = :id")
    Flux<Education> findByUserProfile(Long id);

    @Query("SELECT * FROM education entity WHERE entity.user_profile_id IS NULL")
    Flux<Education> findAllWhereUserProfileIsNull();

    @Override
    <S extends Education> Mono<S> save(S entity);

    @Override
    Flux<Education> findAll();

    @Override
    Mono<Education> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EducationRepositoryInternal {
    <S extends Education> Mono<S> save(S entity);

    Flux<Education> findAllBy(Pageable pageable);

    Flux<Education> findAll();

    Mono<Education> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Education> findAllBy(Pageable pageable, Criteria criteria);
}
