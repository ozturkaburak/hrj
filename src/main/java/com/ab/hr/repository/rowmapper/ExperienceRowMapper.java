package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.Experience;
import com.ab.hr.domain.enumeration.ContractType;
import com.ab.hr.domain.enumeration.WorkType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Experience}, with proper type conversions.
 */
@Service
public class ExperienceRowMapper implements BiFunction<Row, String, Experience> {

    private final ColumnConverter converter;

    public ExperienceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Experience} stored in the database.
     */
    @Override
    public Experience apply(Row row, String prefix) {
        Experience entity = new Experience();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setCompanyName(converter.fromRow(row, prefix + "_company_name", String.class));
        entity.setWorkType(converter.fromRow(row, prefix + "_work_type", WorkType.class));
        entity.setContractType(converter.fromRow(row, prefix + "_contract_type", ContractType.class));
        entity.setOfficeLocation(converter.fromRow(row, prefix + "_office_location", String.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", Instant.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", Instant.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        entity.setUserProfileId(converter.fromRow(row, prefix + "_user_profile_id", Long.class));
        return entity;
    }
}
