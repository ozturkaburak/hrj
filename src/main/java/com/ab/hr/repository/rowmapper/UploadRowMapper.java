package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Upload;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Upload}, with proper type conversions.
 */
@Service
public class UploadRowMapper implements BiFunction<Row, String, Upload> {

    private final ColumnConverter converter;

    public UploadRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Upload} stored in the database.
     */
    @Override
    public Upload apply(Row row, String prefix) {
        Upload entity = new Upload();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFileContentType(converter.fromRow(row, prefix + "_file_content_type", String.class));
        entity.setFile(converter.fromRow(row, prefix + "_file", byte[].class));
        entity.setFileType(converter.fromRow(row, prefix + "_file_type", String.class));
        entity.setUploadDate(converter.fromRow(row, prefix + "_upload_date", Instant.class));
        entity.setUserProfileId(converter.fromRow(row, prefix + "_user_profile_id", Long.class));
        return entity;
    }
}
