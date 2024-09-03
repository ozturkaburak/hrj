package com.ab.hr.repository;

import com.ab.hr.domain.UserLanguage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserLanguage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserLanguageRepository extends ReactiveCrudRepository<UserLanguage, Long>, UserLanguageRepositoryInternal {
    Flux<UserLanguage> findAllBy(Pageable pageable);

    @Query("SELECT * FROM user_language entity WHERE entity.user_id = :id")
    Flux<UserLanguage> findByUser(Long id);

    @Query("SELECT * FROM user_language entity WHERE entity.user_id IS NULL")
    Flux<UserLanguage> findAllWhereUserIsNull();

    @Query("SELECT * FROM user_language entity WHERE entity.language_id = :id")
    Flux<UserLanguage> findByLanguage(Long id);

    @Query("SELECT * FROM user_language entity WHERE entity.language_id IS NULL")
    Flux<UserLanguage> findAllWhereLanguageIsNull();

    @Override
    <S extends UserLanguage> Mono<S> save(S entity);

    @Override
    Flux<UserLanguage> findAll();

    @Override
    Mono<UserLanguage> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserLanguageRepositoryInternal {
    <S extends UserLanguage> Mono<S> save(S entity);

    Flux<UserLanguage> findAllBy(Pageable pageable);

    Flux<UserLanguage> findAll();

    Mono<UserLanguage> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserLanguage> findAllBy(Pageable pageable, Criteria criteria);
}
