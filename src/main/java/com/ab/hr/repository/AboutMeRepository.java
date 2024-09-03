package com.ab.hr.repository;

import com.ab.hr.domain.AboutMe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AboutMe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AboutMeRepository extends ReactiveCrudRepository<AboutMe, Long>, AboutMeRepositoryInternal {
    Flux<AboutMe> findAllBy(Pageable pageable);

    @Query("SELECT * FROM about_me entity WHERE entity.user_profile_id = :id")
    Flux<AboutMe> findByUserProfile(Long id);

    @Query("SELECT * FROM about_me entity WHERE entity.user_profile_id IS NULL")
    Flux<AboutMe> findAllWhereUserProfileIsNull();

    @Override
    <S extends AboutMe> Mono<S> save(S entity);

    @Override
    Flux<AboutMe> findAll();

    @Override
    Mono<AboutMe> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AboutMeRepositoryInternal {
    <S extends AboutMe> Mono<S> save(S entity);

    Flux<AboutMe> findAllBy(Pageable pageable);

    Flux<AboutMe> findAll();

    Mono<AboutMe> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AboutMe> findAllBy(Pageable pageable, Criteria criteria);
}
