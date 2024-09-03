package com.ab.hr.repository;

import com.ab.hr.domain.UserSkill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSkillRepository extends ReactiveCrudRepository<UserSkill, Long>, UserSkillRepositoryInternal {
    Flux<UserSkill> findAllBy(Pageable pageable);

    @Query("SELECT * FROM user_skill entity WHERE entity.skill_id = :id")
    Flux<UserSkill> findBySkill(Long id);

    @Query("SELECT * FROM user_skill entity WHERE entity.skill_id IS NULL")
    Flux<UserSkill> findAllWhereSkillIsNull();

    @Query("SELECT * FROM user_skill entity WHERE entity.user_id = :id")
    Flux<UserSkill> findByUser(Long id);

    @Query("SELECT * FROM user_skill entity WHERE entity.user_id IS NULL")
    Flux<UserSkill> findAllWhereUserIsNull();

    @Override
    <S extends UserSkill> Mono<S> save(S entity);

    @Override
    Flux<UserSkill> findAll();

    @Override
    Mono<UserSkill> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserSkillRepositoryInternal {
    <S extends UserSkill> Mono<S> save(S entity);

    Flux<UserSkill> findAllBy(Pageable pageable);

    Flux<UserSkill> findAll();

    Mono<UserSkill> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserSkill> findAllBy(Pageable pageable, Criteria criteria);
}
