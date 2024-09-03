package com.ab.hr.repository;

import com.ab.hr.domain.Answer;
import com.ab.hr.repository.rowmapper.AnswerRowMapper;
import com.ab.hr.repository.rowmapper.QuestionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Answer entity.
 */
@SuppressWarnings("unused")
class AnswerRepositoryInternalImpl extends SimpleR2dbcRepository<Answer, Long> implements AnswerRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final QuestionRowMapper questionMapper;
    private final UserRowMapper userMapper;
    private final AnswerRowMapper answerMapper;

    private static final Table entityTable = Table.aliased("answer", EntityManager.ENTITY_ALIAS);
    private static final Table questionTable = Table.aliased("question", "question");
    private static final Table userTable = Table.aliased("jhi_user", "e_user");

    public AnswerRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        QuestionRowMapper questionMapper,
        UserRowMapper userMapper,
        AnswerRowMapper answerMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Answer.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;
        this.answerMapper = answerMapper;
    }

    @Override
    public Flux<Answer> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Answer> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AnswerSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(QuestionSqlHelper.getColumns(questionTable, "question"));
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(questionTable)
            .on(Column.create("question_id", entityTable))
            .equals(Column.create("id", questionTable))
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Answer.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Answer> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Answer> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Answer process(Row row, RowMetadata metadata) {
        Answer entity = answerMapper.apply(row, "e");
        entity.setQuestion(questionMapper.apply(row, "question"));
        entity.setUser(userMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends Answer> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
