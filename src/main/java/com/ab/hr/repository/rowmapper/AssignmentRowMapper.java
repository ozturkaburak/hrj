package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Assignment;
import com.ab.hr.domain.enumeration.AssignmentType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Assignment}, with proper type conversions.
 */
@Service
public class AssignmentRowMapper implements BiFunction<Row, String, Assignment> {

    private final ColumnConverter converter;

    public AssignmentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Assignment} stored in the database.
     */
    @Override
    public Assignment apply(Row row, String prefix) {
        Assignment entity = new Assignment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", AssignmentType.class));
        entity.setVisible(converter.fromRow(row, prefix + "_visible", Boolean.class));
        entity.setHashtags(converter.fromRow(row, prefix + "_hashtags", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        return entity;
    }
}
