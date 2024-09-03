package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.UserAssignment;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserAssignment}, with proper type conversions.
 */
@Service
public class UserAssignmentRowMapper implements BiFunction<Row, String, UserAssignment> {

    private final ColumnConverter converter;

    public UserAssignmentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserAssignment} stored in the database.
     */
    @Override
    public UserAssignment apply(Row row, String prefix) {
        UserAssignment entity = new UserAssignment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAssignedAt(converter.fromRow(row, prefix + "_assigned_at", Instant.class));
        entity.setJoinedAt(converter.fromRow(row, prefix + "_joined_at", Instant.class));
        entity.setFinishedAt(converter.fromRow(row, prefix + "_finished_at", Instant.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setAssignmentId(converter.fromRow(row, prefix + "_assignment_id", Long.class));
        return entity;
    }
}
