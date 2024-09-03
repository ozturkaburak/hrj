package com.ab.hr.repository;

import com.ab.hr.domain.Assignment;
import com.ab.hr.domain.Question;
import com.ab.hr.repository.rowmapper.AssignmentRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Assignment entity.
 */
@SuppressWarnings("unused")
class AssignmentRepositoryInternalImpl extends SimpleR2dbcRepository<Assignment, Long> implements AssignmentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AssignmentRowMapper assignmentMapper;

    private static final Table entityTable = Table.aliased("assignment", EntityManager.ENTITY_ALIAS);

    private static final EntityManager.LinkTable questionsLink = new EntityManager.LinkTable(
        "rel_assignment__questions",
        "assignment_id",
        "questions_id"
    );

    public AssignmentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AssignmentRowMapper assignmentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Assignment.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.assignmentMapper = assignmentMapper;
    }

    @Override
    public Flux<Assignment> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Assignment> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AssignmentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Assignment.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Assignment> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Assignment> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Assignment> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Assignment> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Assignment> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Assignment process(Row row, RowMetadata metadata) {
        Assignment entity = assignmentMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Assignment> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Assignment> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(questionsLink, entity.getId(), entity.getQuestions().stream().map(Question::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(questionsLink, entityId);
    }
}
