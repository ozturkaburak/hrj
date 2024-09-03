package com.ab.hr.repository;

import com.ab.hr.domain.UserSkill;
import com.ab.hr.repository.rowmapper.SkillRowMapper;
import com.ab.hr.repository.rowmapper.UserRowMapper;
import com.ab.hr.repository.rowmapper.UserSkillRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UserSkill entity.
 */
@SuppressWarnings("unused")
class UserSkillRepositoryInternalImpl extends SimpleR2dbcRepository<UserSkill, Long> implements UserSkillRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SkillRowMapper skillMapper;
    private final UserRowMapper userMapper;
    private final UserSkillRowMapper userskillMapper;

    private static final Table entityTable = Table.aliased("user_skill", EntityManager.ENTITY_ALIAS);
    private static final Table skillTable = Table.aliased("skill", "skill");
    private static final Table userTable = Table.aliased("jhi_user", "e_user");

    public UserSkillRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SkillRowMapper skillMapper,
        UserRowMapper userMapper,
        UserSkillRowMapper userskillMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserSkill.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.skillMapper = skillMapper;
        this.userMapper = userMapper;
        this.userskillMapper = userskillMapper;
    }

    @Override
    public Flux<UserSkill> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserSkill> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserSkillSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SkillSqlHelper.getColumns(skillTable, "skill"));
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(skillTable)
            .on(Column.create("skill_id", entityTable))
            .equals(Column.create("id", skillTable))
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserSkill.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserSkill> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserSkill> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private UserSkill process(Row row, RowMetadata metadata) {
        UserSkill entity = userskillMapper.apply(row, "e");
        entity.setSkill(skillMapper.apply(row, "skill"));
        entity.setUser(userMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends UserSkill> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
