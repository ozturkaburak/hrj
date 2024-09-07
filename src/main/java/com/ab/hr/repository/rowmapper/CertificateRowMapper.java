package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Certificate;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Certificate}, with proper type conversions.
 */
@Service
public class CertificateRowMapper implements BiFunction<Row, String, Certificate> {

    private final ColumnConverter converter;

    public CertificateRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Certificate} stored in the database.
     */
    @Override
    public Certificate apply(Row row, String prefix) {
        Certificate entity = new Certificate();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", LocalDate.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", LocalDate.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        entity.setUserProfileId(converter.fromRow(row, prefix + "_user_profile_id", Long.class));
        return entity;
    }
}
