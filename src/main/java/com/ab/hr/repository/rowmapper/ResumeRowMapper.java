package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Resume;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Resume}, with proper type conversions.
 */
@Service
public class ResumeRowMapper implements BiFunction<Row, String, Resume> {

    private final ColumnConverter converter;

    public ResumeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Resume} stored in the database.
     */
    @Override
    public Resume apply(Row row, String prefix) {
        Resume entity = new Resume();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFileContentType(converter.fromRow(row, prefix + "_file_content_type", String.class));
        entity.setFile(converter.fromRow(row, prefix + "_file", byte[].class));
        entity.setFileType(converter.fromRow(row, prefix + "_file_type", String.class));
        entity.setUploadDate(converter.fromRow(row, prefix + "_upload_date", Instant.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
