package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Question;
import com.ab.hr.domain.enumeration.QuestionType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Question}, with proper type conversions.
 */
@Service
public class QuestionRowMapper implements BiFunction<Row, String, Question> {

    private final ColumnConverter converter;

    public QuestionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Question} stored in the database.
     */
    @Override
    public Question apply(Row row, String prefix) {
        Question entity = new Question();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setOptions(converter.fromRow(row, prefix + "_options", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", QuestionType.class));
        entity.setCorrectAnswer(converter.fromRow(row, prefix + "_correct_answer", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        return entity;
    }
}
