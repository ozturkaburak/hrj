package com.ab.hr.service.mapper;

import com.ab.hr.domain.Company;
import com.ab.hr.domain.JobPosting;
import com.ab.hr.service.dto.CompanyDTO;
import com.ab.hr.service.dto.JobPostingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobPosting} and its DTO {@link JobPostingDTO}.
 */
@Mapper(componentModel = "spring")
public interface JobPostingMapper extends EntityMapper<JobPostingDTO, JobPosting> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    JobPostingDTO toDto(JobPosting s);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
