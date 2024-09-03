package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Education;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Education}, with proper type conversions.
 */
@Service
public class EducationRowMapper implements BiFunction<Row, String, Education> {

    private final ColumnConverter converter;

    public EducationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Education} stored in the database.
     */
    @Override
    public Education apply(Row row, String prefix) {
        Education entity = new Education();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSchoolName(converter.fromRow(row, prefix + "_school_name", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setDegree(converter.fromRow(row, prefix + "_degree", String.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", Instant.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", Instant.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setActivities(converter.fromRow(row, prefix + "_activities", String.class));
        entity.setClubs(converter.fromRow(row, prefix + "_clubs", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        entity.setUserProfileId(converter.fromRow(row, prefix + "_user_profile_id", Long.class));
        return entity;
    }
}
