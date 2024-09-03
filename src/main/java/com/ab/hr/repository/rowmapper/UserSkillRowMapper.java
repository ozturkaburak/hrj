package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.UserSkill;
import com.ab.hr.domain.enumeration.SkillLevel;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserSkill}, with proper type conversions.
 */
@Service
public class UserSkillRowMapper implements BiFunction<Row, String, UserSkill> {

    private final ColumnConverter converter;

    public UserSkillRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserSkill} stored in the database.
     */
    @Override
    public UserSkill apply(Row row, String prefix) {
        UserSkill entity = new UserSkill();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setYear(converter.fromRow(row, prefix + "_year", Integer.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", SkillLevel.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        entity.setSkillId(converter.fromRow(row, prefix + "_skill_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
