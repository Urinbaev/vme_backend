package ru.sibintek.vme.company.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.company.dto.CompanyDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    CompanyDto toDto(Company company);

    Company toEntity(CompanyDto companyDto);

}