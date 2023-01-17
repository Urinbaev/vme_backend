package ru.sibintek.vme.driver.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sibintek.vme.common.security.AuthenticationFacade;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.company.service.CompanyService;
import ru.sibintek.vme.driver.dto.DriverDto;
import ru.sibintek.vme.driver.mapper.DriverMapper;
import ru.sibintek.vme.driver.service.DriverService;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CLIENT', 'ADMIN', 'SUPPORT')")
public class DriverController {
    private final DriverMapper mapper;
    private final DriverService driverService;
    private final CompanyService companyService;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping
    public DriverDto addDriver(@RequestBody DriverDto driverDto) {
        Long companyId = authenticationFacade.getCurrentUser().getCompanyId();
        return mapper.toDto(driverService.addDriver(companyId, driverDto));
    }

    @GetMapping("/{id}")
    public DriverDto getDriverById(@PathVariable Long id) {
        return driverService.getDriver(id);
    }

    @GetMapping
    public Page<DriverDto> getDrivers(@RequestParam(required = false) String filter, Pageable pageable) {
        Long companyId = authenticationFacade.getCurrentUser().getCompanyId();
        return driverService.getDrivers(companyId, filter, pageable);
    }

    @PutMapping("/{id}")
    public DriverDto updateDriver(@PathVariable Long id, @RequestBody DriverDto driverDto) {
        Long companyId = authenticationFacade.getCurrentUser().getCompanyId();
        return driverService.updateDriver(id, companyId, driverDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable Long id) {
        Long companyId = authenticationFacade.getCurrentUser().getCompanyId();
        driverService.deleteDriver(id, companyId);
    }

    @SneakyThrows
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestPart("file") MultipartFile file) {
        Long companyId = authenticationFacade.getCurrentUser().getCompanyId();
        Company company = companyService.getCompanyById(companyId);
        driverService.upload(file.getInputStream(), company);
    }

}