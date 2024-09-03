package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Answer;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Answer}, with proper type conversions.
 */
@Service
public class AnswerRowMapper implements BiFunction<Row, String, Answer> {

    private final ColumnConverter converter;

    public AnswerRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Answer} stored in the database.
     */
    @Override
    public Answer apply(Row row, String prefix) {
        Answer entity = new Answer();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setAnsweredAt(converter.fromRow(row, prefix + "_answered_at", Instant.class));
        entity.setQuestionId(converter.fromRow(row, prefix + "_question_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
