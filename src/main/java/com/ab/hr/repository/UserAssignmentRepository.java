package com.ab.hr.repository;

import com.ab.hr.domain.UserAssignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAssignmentRepository extends ReactiveCrudRepository<UserAssignment, Long>, UserAssignmentRepositoryInternal {
    Flux<UserAssignment> findAllBy(Pageable pageable);

    @Query("SELECT * FROM user_assignment entity WHERE entity.user_id = :id")
    Flux<UserAssignment> findByUser(Long id);

    @Query("SELECT * FROM user_assignment entity WHERE entity.user_id IS NULL")
    Flux<UserAssignment> findAllWhereUserIsNull();

    @Query("SELECT * FROM user_assignment entity WHERE entity.assignment_id = :id")
    Flux<UserAssignment> findByAssignment(Long id);

    @Query("SELECT * FROM user_assignment entity WHERE entity.assignment_id IS NULL")
    Flux<UserAssignment> findAllWhereAssignmentIsNull();

    @Override
    <S extends UserAssignment> Mono<S> save(S entity);

    @Override
    Flux<UserAssignment> findAll();

    @Override
    Mono<UserAssignment> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserAssignmentRepositoryInternal {
    <S extends UserAssignment> Mono<S> save(S entity);

    Flux<UserAssignment> findAllBy(Pageable pageable);

    Flux<UserAssignment> findAll();

    Mono<UserAssignment> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserAssignment> findAllBy(Pageable pageable, Criteria criteria);
}
