package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.JobPosting;
import com.ab.hr.domain.enumeration.JobStatus;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link JobPosting}, with proper type conversions.
 */
@Service
public class JobPostingRowMapper implements BiFunction<Row, String, JobPosting> {

    private final ColumnConverter converter;

    public JobPostingRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link JobPosting} stored in the database.
     */
    @Override
    public JobPosting apply(Row row, String prefix) {
        JobPosting entity = new JobPosting();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setLocation(converter.fromRow(row, prefix + "_location", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", JobStatus.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setExpireDate(converter.fromRow(row, prefix + "_expire_date", Instant.class));
        return entity;
    }
}
