package com.ab.hr.service.mapper;

import com.ab.hr.domain.City;
import com.ab.hr.domain.Country;
import com.ab.hr.service.dto.CityDTO;
import com.ab.hr.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryId")
    CityDTO toDto(City s);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);
}
