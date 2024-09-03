package com.ab.hr.repository;

import com.ab.hr.domain.Skill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Skill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillRepository extends ReactiveCrudRepository<Skill, Long>, SkillRepositoryInternal {
    Flux<Skill> findAllBy(Pageable pageable);

    @Query("SELECT * FROM skill entity WHERE entity.experience_id = :id")
    Flux<Skill> findByExperience(Long id);

    @Query("SELECT * FROM skill entity WHERE entity.experience_id IS NULL")
    Flux<Skill> findAllWhereExperienceIsNull();

    @Query("SELECT * FROM skill entity WHERE entity.id not in (select user_skill_id from user_skill)")
    Flux<Skill> findAllWhereUserSkillIsNull();

    @Override
    <S extends Skill> Mono<S> save(S entity);

    @Override
    Flux<Skill> findAll();

    @Override
    Mono<Skill> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SkillRepositoryInternal {
    <S extends Skill> Mono<S> save(S entity);

    Flux<Skill> findAllBy(Pageable pageable);

    Flux<Skill> findAll();

    Mono<Skill> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Skill> findAllBy(Pageable pageable, Criteria criteria);
}
