package com.ab.hr.repository;

import com.ab.hr.domain.Upload;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Upload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UploadRepository extends ReactiveCrudRepository<Upload, Long>, UploadRepositoryInternal {
    Flux<Upload> findAllBy(Pageable pageable);

    @Query("SELECT * FROM upload entity WHERE entity.user_profile_id = :id")
    Flux<Upload> findByUserProfile(Long id);

    @Query("SELECT * FROM upload entity WHERE entity.user_profile_id IS NULL")
    Flux<Upload> findAllWhereUserProfileIsNull();

    @Override
    <S extends Upload> Mono<S> save(S entity);

    @Override
    Flux<Upload> findAll();

    @Override
    Mono<Upload> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UploadRepositoryInternal {
    <S extends Upload> Mono<S> save(S entity);

    Flux<Upload> findAllBy(Pageable pageable);

    Flux<Upload> findAll();

    Mono<Upload> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Upload> findAllBy(Pageable pageable, Criteria criteria);
}
