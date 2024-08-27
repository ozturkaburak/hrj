package com.ab.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ab.hr.domain.Resume} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResumeDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] file;

    private String fileContentType;

    @NotNull(message = "must not be null")
    private String fileType;

    @NotNull(message = "must not be null")
    private Instant uploadDate;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResumeDTO)) {
            return false;
        }

        ResumeDTO resumeDTO = (ResumeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resumeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResumeDTO{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
