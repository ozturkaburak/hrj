package com.ab.hr.repository;

import com.ab.hr.domain.UserAssignment;
import com.ab.hr.repository.rowmapper.AssignmentRowMapper;
import com.ab.hr.repository.rowmapper.UserAssignmentRowMapper;
import com.ab.hr.repository.rowmapper.UserRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UserAssignment entity.
 */
@SuppressWarnings("unused")
class UserAssignmentRepositoryInternalImpl extends SimpleR2dbcRepository<UserAssignment, Long> implements UserAssignmentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final AssignmentRowMapper assignmentMapper;
    private final UserAssignmentRowMapper userassignmentMapper;

    private static final Table entityTable = Table.aliased("user_assignment", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("jhi_user", "e_user");
    private static final Table assignmentTable = Table.aliased("assignment", "assignment");

    public UserAssignmentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        AssignmentRowMapper assignmentMapper,
        UserAssignmentRowMapper userassignmentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserAssignment.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.assignmentMapper = assignmentMapper;
        this.userassignmentMapper = userassignmentMapper;
    }

    @Override
    public Flux<UserAssignment> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserAssignment> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserAssignmentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        columns.addAll(AssignmentSqlHelper.getColumns(assignmentTable, "assignment"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable))
            .leftOuterJoin(assignmentTable)
            .on(Column.create("assignment_id", entityTable))
            .equals(Column.create("id", assignmentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserAssignment.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserAssignment> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserAssignment> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private UserAssignment process(Row row, RowMetadata metadata) {
        UserAssignment entity = userassignmentMapper.apply(row, "e");
        entity.setUser(userMapper.apply(row, "user"));
        entity.setAssignment(assignmentMapper.apply(row, "assignment"));
        return entity;
    }

    @Override
    public <S extends UserAssignment> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
