package ru.sibintek.vme.company.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sibintek.vme.company.dto.CompanyDto;
import ru.sibintek.vme.company.service.CompanyService;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public CompanyDto addNewCompany(@RequestBody CompanyDto companyDto) {
        return companyService.saveCompany(companyDto);
    }

    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable Long id) {
        return companyService.getCompany(id);
    }

    @GetMapping
    public Page<CompanyDto> getCompanies(@RequestParam Map<String, String> filters, Pageable pageable) {
        return companyService.getCompanies(filters, pageable);
    }

    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto){
        return companyService.updateCompany(id, companyDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }

}