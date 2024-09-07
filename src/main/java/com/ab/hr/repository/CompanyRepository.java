package com.ab.hr.repository;

import com.ab.hr.domain.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends ReactiveCrudRepository<Company, Long>, CompanyRepositoryInternal {
    Flux<Company> findAllBy(Pageable pageable);

    @Query("SELECT * FROM company entity WHERE entity.city_id = :id")
    Flux<Company> findByCity(Long id);

    @Query("SELECT * FROM company entity WHERE entity.city_id IS NULL")
    Flux<Company> findAllWhereCityIsNull();

    @Query("SELECT * FROM company entity WHERE entity.id not in (select job_posting_id from job_posting)")
    Flux<Company> findAllWhereJobPostingIsNull();

    @Query("SELECT * FROM company entity WHERE entity.id not in (select experience_id from experience)")
    Flux<Company> findAllWhereExperienceIsNull();

    @Override
    <S extends Company> Mono<S> save(S entity);

    @Override
    Flux<Company> findAll();

    @Override
    Mono<Company> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CompanyRepositoryInternal {
    <S extends Company> Mono<S> save(S entity);

    Flux<Company> findAllBy(Pageable pageable);

    Flux<Company> findAll();

    Mono<Company> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Company> findAllBy(Pageable pageable, Criteria criteria);
}
