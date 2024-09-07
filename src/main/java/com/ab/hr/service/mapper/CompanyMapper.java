package com.ab.hr.service.mapper;

import com.ab.hr.domain.City;
import com.ab.hr.domain.Company;
import com.ab.hr.service.dto.CityDTO;
import com.ab.hr.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityId")
    CompanyDTO toDto(Company s);

    @Named("cityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CityDTO toDtoCityId(City city);
}
