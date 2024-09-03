package com.ab.hr.repository;

import com.ab.hr.domain.UserLanguage;
import com.ab.hr.repository.rowmapper.LanguageRowMapper;
import com.ab.hr.repository.rowmapper.UserLanguageRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UserLanguage entity.
 */
@SuppressWarnings("unused")
class UserLanguageRepositoryInternalImpl extends SimpleR2dbcRepository<UserLanguage, Long> implements UserLanguageRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final LanguageRowMapper languageMapper;
    private final UserLanguageRowMapper userlanguageMapper;

    private static final Table entityTable = Table.aliased("user_language", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("jhi_user", "e_user");
    private static final Table languageTable = Table.aliased("language", "language");

    public UserLanguageRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        LanguageRowMapper languageMapper,
        UserLanguageRowMapper userlanguageMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserLanguage.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.languageMapper = languageMapper;
        this.userlanguageMapper = userlanguageMapper;
    }

    @Override
    public Flux<UserLanguage> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserLanguage> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserLanguageSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        columns.addAll(LanguageSqlHelper.getColumns(languageTable, "language"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable))
            .leftOuterJoin(languageTable)
            .on(Column.create("language_id", entityTable))
            .equals(Column.create("id", languageTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserLanguage.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserLanguage> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserLanguage> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private UserLanguage process(Row row, RowMetadata metadata) {
        UserLanguage entity = userlanguageMapper.apply(row, "e");
        entity.setUser(userMapper.apply(row, "user"));
        entity.setLanguage(languageMapper.apply(row, "language"));
        return entity;
    }

    @Override
    public <S extends UserLanguage> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
