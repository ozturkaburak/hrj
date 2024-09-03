package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Language;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Language}, with proper type conversions.
 */
@Service
public class LanguageRowMapper implements BiFunction<Row, String, Language> {

    private final ColumnConverter converter;

    public LanguageRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Language} stored in the database.
     */
    @Override
    public Language apply(Row row, String prefix) {
        Language entity = new Language();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        return entity;
    }
}
