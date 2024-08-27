package com.ab.hr.repository;

import com.ab.hr.domain.Resume;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Resume entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResumeRepository extends ReactiveCrudRepository<Resume, Long>, ResumeRepositoryInternal {
    Flux<Resume> findAllBy(Pageable pageable);

    @Query("SELECT * FROM resume entity WHERE entity.user_id = :id")
    Flux<Resume> findByUser(Long id);

    @Query("SELECT * FROM resume entity WHERE entity.user_id IS NULL")
    Flux<Resume> findAllWhereUserIsNull();

    @Override
    <S extends Resume> Mono<S> save(S entity);

    @Override
    Flux<Resume> findAll();

    @Override
    Mono<Resume> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ResumeRepositoryInternal {
    <S extends Resume> Mono<S> save(S entity);

    Flux<Resume> findAllBy(Pageable pageable);

    Flux<Resume> findAll();

    Mono<Resume> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Resume> findAllBy(Pageable pageable, Criteria criteria);
}
