package com.ab.hr.repository;

import com.ab.hr.domain.Experience;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Experience entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExperienceRepository extends ReactiveCrudRepository<Experience, Long>, ExperienceRepositoryInternal {
    Flux<Experience> findAllBy(Pageable pageable);

    @Query("SELECT * FROM experience entity WHERE entity.company_id = :id")
    Flux<Experience> findByCompany(Long id);

    @Query("SELECT * FROM experience entity WHERE entity.company_id IS NULL")
    Flux<Experience> findAllWhereCompanyIsNull();

    @Query("SELECT * FROM experience entity WHERE entity.user_profile_id = :id")
    Flux<Experience> findByUserProfile(Long id);

    @Query("SELECT * FROM experience entity WHERE entity.user_profile_id IS NULL")
    Flux<Experience> findAllWhereUserProfileIsNull();

    @Override
    <S extends Experience> Mono<S> save(S entity);

    @Override
    Flux<Experience> findAll();

    @Override
    Mono<Experience> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ExperienceRepositoryInternal {
    <S extends Experience> Mono<S> save(S entity);

    Flux<Experience> findAllBy(Pageable pageable);

    Flux<Experience> findAll();

    Mono<Experience> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Experience> findAllBy(Pageable pageable, Criteria criteria);
}
