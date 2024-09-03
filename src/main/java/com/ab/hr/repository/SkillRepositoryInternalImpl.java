package com.ab.hr.repository;

import com.ab.hr.domain.Skill;
import com.ab.hr.repository.rowmapper.ExperienceRowMapper;
import com.ab.hr.repository.rowmapper.SkillRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Skill entity.
 */
@SuppressWarnings("unused")
class SkillRepositoryInternalImpl extends SimpleR2dbcRepository<Skill, Long> implements SkillRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ExperienceRowMapper experienceMapper;
    private final SkillRowMapper skillMapper;

    private static final Table entityTable = Table.aliased("skill", EntityManager.ENTITY_ALIAS);
    private static final Table experienceTable = Table.aliased("experience", "experience");

    public SkillRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ExperienceRowMapper experienceMapper,
        SkillRowMapper skillMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Skill.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.experienceMapper = experienceMapper;
        this.skillMapper = skillMapper;
    }

    @Override
    public Flux<Skill> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Skill> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SkillSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ExperienceSqlHelper.getColumns(experienceTable, "experience"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(experienceTable)
            .on(Column.create("experience_id", entityTable))
            .equals(Column.create("id", experienceTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Skill.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Skill> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Skill> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Skill process(Row row, RowMetadata metadata) {
        Skill entity = skillMapper.apply(row, "e");
        entity.setExperience(experienceMapper.apply(row, "experience"));
        return entity;
    }

    @Override
    public <S extends Skill> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
