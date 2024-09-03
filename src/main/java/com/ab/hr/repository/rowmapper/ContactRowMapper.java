package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Contact;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Contact}, with proper type conversions.
 */
@Service
public class ContactRowMapper implements BiFunction<Row, String, Contact> {

    private final ColumnConverter converter;

    public ContactRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Contact} stored in the database.
     */
    @Override
    public Contact apply(Row row, String prefix) {
        Contact entity = new Contact();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSecondaryEmail(converter.fromRow(row, prefix + "_secondary_email", String.class));
        entity.setPhoneNumber(converter.fromRow(row, prefix + "_phone_number", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setCityId(converter.fromRow(row, prefix + "_city_id", Long.class));
        return entity;
    }
}
