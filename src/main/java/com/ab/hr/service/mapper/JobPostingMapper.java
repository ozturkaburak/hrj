package com.ab.hr.service.mapper;

import com.ab.hr.domain.JobPosting;
import com.ab.hr.service.dto.JobPostingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobPosting} and its DTO {@link JobPostingDTO}.
 */
@Mapper(componentModel = "spring")
public interface JobPostingMapper extends EntityMapper<JobPostingDTO, JobPosting> {}
