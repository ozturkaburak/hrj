package com.ab.hr.repository;

import com.ab.hr.domain.JobPosting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the JobPosting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobPostingRepository extends ReactiveCrudRepository<JobPosting, Long>, JobPostingRepositoryInternal {
    Flux<JobPosting> findAllBy(Pageable pageable);

    @Override
    <S extends JobPosting> Mono<S> save(S entity);

    @Override
    Flux<JobPosting> findAll();

    @Override
    Mono<JobPosting> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface JobPostingRepositoryInternal {
    <S extends JobPosting> Mono<S> save(S entity);

    Flux<JobPosting> findAllBy(Pageable pageable);

    Flux<JobPosting> findAll();

    Mono<JobPosting> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<JobPosting> findAllBy(Pageable pageable, Criteria criteria);
}
