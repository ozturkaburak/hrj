package com.ab.hr.domain;

import com.ab.hr.domain.enumeration.LanguageLevel;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UserLanguage.
 */
@Table("user_language")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserLanguage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("level")
    private LanguageLevel level;

    @Column("native_language")
    private Boolean nativeLanguage;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("deleted_at")
    private Instant deletedAt;

    @Transient
    private User user;

    @Transient
    private Language language;

    @Column("user_id")
    private Long userId;

    @Column("language_id")
    private Long languageId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserLanguage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LanguageLevel getLevel() {
        return this.level;
    }

    public UserLanguage level(LanguageLevel level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(LanguageLevel level) {
        this.level = level;
    }

    public Boolean getNativeLanguage() {
        return this.nativeLanguage;
    }

    public UserLanguage nativeLanguage(Boolean nativeLanguage) {
        this.setNativeLanguage(nativeLanguage);
        return this;
    }

    public void setNativeLanguage(Boolean nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UserLanguage createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public UserLanguage updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public UserLanguage deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public UserLanguage user(User user) {
        this.setUser(user);
        return this;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
        this.languageId = language != null ? language.getId() : null;
    }

    public UserLanguage language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long user) {
        this.userId = user;
    }

    public Long getLanguageId() {
        return this.languageId;
    }

    public void setLanguageId(Long language) {
        this.languageId = language;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserLanguage)) {
            return false;
        }
        return getId() != null && getId().equals(((UserLanguage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserLanguage{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            ", nativeLanguage='" + getNativeLanguage() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
