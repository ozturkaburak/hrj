package com.ab.hr.repository;

import com.ab.hr.domain.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Assignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssignmentRepository extends ReactiveCrudRepository<Assignment, Long>, AssignmentRepositoryInternal {
    Flux<Assignment> findAllBy(Pageable pageable);

    @Override
    Mono<Assignment> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Assignment> findAllWithEagerRelationships();

    @Override
    Flux<Assignment> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM assignment entity JOIN rel_assignment__questions joinTable ON entity.id = joinTable.questions_id WHERE joinTable.questions_id = :id"
    )
    Flux<Assignment> findByQuestions(Long id);

    @Query("SELECT * FROM assignment entity WHERE entity.id not in (select user_assignment_id from user_assignment)")
    Flux<Assignment> findAllWhereUserAssignmentIsNull();

    @Override
    <S extends Assignment> Mono<S> save(S entity);

    @Override
    Flux<Assignment> findAll();

    @Override
    Mono<Assignment> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AssignmentRepositoryInternal {
    <S extends Assignment> Mono<S> save(S entity);

    Flux<Assignment> findAllBy(Pageable pageable);

    Flux<Assignment> findAll();

    Mono<Assignment> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Assignment> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Assignment> findOneWithEagerRelationships(Long id);

    Flux<Assignment> findAllWithEagerRelationships();

    Flux<Assignment> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
