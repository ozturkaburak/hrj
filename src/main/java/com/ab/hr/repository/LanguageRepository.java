package com.ab.hr.repository;

import com.ab.hr.domain.Language;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Language entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageRepository extends ReactiveCrudRepository<Language, Long>, LanguageRepositoryInternal {
    Flux<Language> findAllBy(Pageable pageable);

    @Query("SELECT * FROM language entity WHERE entity.id not in (select user_language_id from user_language)")
    Flux<Language> findAllWhereUserLanguageIsNull();

    @Override
    <S extends Language> Mono<S> save(S entity);

    @Override
    Flux<Language> findAll();

    @Override
    Mono<Language> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LanguageRepositoryInternal {
    <S extends Language> Mono<S> save(S entity);

    Flux<Language> findAllBy(Pageable pageable);

    Flux<Language> findAll();

    Mono<Language> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Language> findAllBy(Pageable pageable, Criteria criteria);
}
