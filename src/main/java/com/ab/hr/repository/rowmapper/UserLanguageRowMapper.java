package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.UserLanguage;
import com.ab.hr.domain.enumeration.LanguageLevel;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserLanguage}, with proper type conversions.
 */
@Service
public class UserLanguageRowMapper implements BiFunction<Row, String, UserLanguage> {

    private final ColumnConverter converter;

    public UserLanguageRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserLanguage} stored in the database.
     */
    @Override
    public UserLanguage apply(Row row, String prefix) {
        UserLanguage entity = new UserLanguage();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", LanguageLevel.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setLanguageId(converter.fromRow(row, prefix + "_language_id", Long.class));
        return entity;
    }
}
