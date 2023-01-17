package ru.sibintek.vme.company.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sibintek.vme.common.domain.EntityStatus;
import ru.sibintek.vme.common.exception.RecordNotFoundException;
import ru.sibintek.vme.common.security.AuthenticationFacade;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.company.dto.CompanyDto;
import ru.sibintek.vme.company.mapper.CompanyMapper;
import ru.sibintek.vme.company.repository.CompanyRepository;
import ru.sibintek.vme.company.service.CompanyService;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Objects;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    @Transactional
    public CompanyDto saveCompany(CompanyDto companyDto) {
        Company company = companyMapper.toEntity(companyDto);
        Company saved = companyRepository.save(company);
        return companyMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDto getCompany(Long id) {
        return companyMapper.toDto(getCompanyById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyDto> getCompanies(Map<String, String> filters, Pageable pageable) {
        Company probe = new Company();

        if (Objects.nonNull(filters.get("name"))) {
            probe.setName(filters.get("name"));
        }

        if (Objects.nonNull(filters.get("inn"))) {
            probe.setInn(filters.get("inn"));
        }

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", contains().ignoreCase());

        return companyRepository.findAll(Example.of(probe, matcher), pageable).map(companyMapper::toDto);
    }

    @Override
    @Transactional
    public CompanyDto updateCompany(Long id, CompanyDto companyDto) {
        Company company = getCompanyById(id);
        company.setName(companyDto.getName());
        company.setInn(companyDto.getInn());
        company.setKpp(companyDto.getKpp());
        company.setOgrn(companyDto.getOgrn());
        company.setAddress(companyDto.getAddress());
        company.setPhone(companyDto.getPhone());
        Company saved = companyRepository.save(company);
        return companyMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteCompany(Long id) {
        Company company = getCompanyById(id);
        company.setStatus(EntityStatus.INACTIVE);
        companyRepository.save(company);
    }

    @Override
    @Transactional(readOnly = true)
    public Company getCompanyById(Long id) {
        try {
            return companyRepository.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            throw new RecordNotFoundException(String.format("Company with ID: %s was not found", id));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Company getOwnerCompany() {
        var companyId = authenticationFacade.getCurrentUser().getCompanyId();
        return getCompanyById(companyId);
    }
}

