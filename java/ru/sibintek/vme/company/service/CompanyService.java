package ru.sibintek.vme.company.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.company.dto.CompanyDto;

import java.util.Map;

public interface CompanyService {

    CompanyDto saveCompany(CompanyDto companyDto);

    CompanyDto getCompany(Long id);

    Page<CompanyDto> getCompanies(Map<String, String> filters, Pageable pageable);

    CompanyDto updateCompany(Long id, CompanyDto companyDto);

    void deleteCompany(Long id);

    Company getCompanyById(Long companyId);

    Company getOwnerCompany();
}
