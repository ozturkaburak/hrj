package com.ab.hr.repository;

import com.ab.hr.domain.Education;
import com.ab.hr.repository.rowmapper.EducationRowMapper;
import com.ab.hr.repository.rowmapper.UserProfileRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Education entity.
 */
@SuppressWarnings("unused")
class EducationRepositoryInternalImpl extends SimpleR2dbcRepository<Education, Long> implements EducationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserProfileRowMapper userprofileMapper;
    private final EducationRowMapper educationMapper;

    private static final Table entityTable = Table.aliased("education", EntityManager.ENTITY_ALIAS);
    private static final Table userProfileTable = Table.aliased("user_profile", "userProfile");

    public EducationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserProfileRowMapper userprofileMapper,
        EducationRowMapper educationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Education.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userprofileMapper = userprofileMapper;
        this.educationMapper = educationMapper;
    }

    @Override
    public Flux<Education> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Education> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EducationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserProfileSqlHelper.getColumns(userProfileTable, "userProfile"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userProfileTable)
            .on(Column.create("user_profile_id", entityTable))
            .equals(Column.create("id", userProfileTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Education.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Education> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Education> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Education process(Row row, RowMetadata metadata) {
        Education entity = educationMapper.apply(row, "e");
        entity.setUserProfile(userprofileMapper.apply(row, "userProfile"));
        return entity;
    }

    @Override
    public <S extends Education> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
