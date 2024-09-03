package com.ab.hr.repository.rowmapper;

import com.ab.hr.domain.AboutMe;
import com.ab.hr.domain.enumeration.SocialMediaType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AboutMe}, with proper type conversions.
 */
@Service
public class AboutMeRowMapper implements BiFunction<Row, String, AboutMe> {

    private final ColumnConverter converter;

    public AboutMeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AboutMe} stored in the database.
     */
    @Override
    public AboutMe apply(Row row, String prefix) {
        AboutMe entity = new AboutMe();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSocialMedia(converter.fromRow(row, prefix + "_social_media", SocialMediaType.class));
        entity.setUrl(converter.fromRow(row, prefix + "_url", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", Instant.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", Instant.class));
        entity.setUserProfileId(converter.fromRow(row, prefix + "_user_profile_id", Long.class));
        return entity;
    }
}
