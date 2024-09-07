package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Company;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Company}, with proper type conversions.
 */
@Service
public class CompanyRowMapper implements BiFunction<Row, String, Company> {

    private final ColumnConverter converter;

    public CompanyRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Company} stored in the database.
     */
    @Override
    public Company apply(Row row, String prefix) {
        Company entity = new Company();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setLogo(converter.fromRow(row, prefix + "_logo", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        entity.setActive(converter.fromRow(row, prefix + "_active", Boolean.class));
        entity.setCityId(converter.fromRow(row, prefix + "_city_id", Long.class));
        return entity;
    }
}
