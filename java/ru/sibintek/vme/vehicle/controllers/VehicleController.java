package ru.sibintek.vme.vehicle.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sibintek.vme.common.security.AuthenticationFacade;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.company.service.CompanyService;
import ru.sibintek.vme.driver.dto.DriverDto;
import ru.sibintek.vme.driver.service.DriverService;
import ru.sibintek.vme.vehicle.domain.Vehicle;
import ru.sibintek.vme.vehicle.domain.VehicleType;
import ru.sibintek.vme.vehicle.dto.VehicleDto;
import ru.sibintek.vme.vehicle.dto.VehicleTypeDto;
import ru.sibintek.vme.vehicle.mapper.VehicleMapper;
import ru.sibintek.vme.vehicle.mapper.VehicleTypeMapper;
import ru.sibintek.vme.vehicle.services.VehicleService;
import ru.sibintek.vme.vehicle.services.VehicleTypeService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CLIENT', 'ADMIN', 'SUPPORT')")
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleTypeService vehicleTypeService;
    private final CompanyService companyService;
    private final VehicleMapper vehicleMapper;
    private final VehicleTypeMapper vehicleTypeMapper;
    private final DriverService driverService;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping
    VehicleDto createVehicle(@RequestBody VehicleDto vehicleDto) {
        Long companyId = authenticationFacade.getCurrentUser().getCompanyId();
        Company company = companyService.getCompanyById(companyId);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        if (Objects.nonNull(vehicleDto.getDrivers())) {
            Set<Long> driverIds = vehicleDto.getDrivers().stream().map(DriverDto::getId).collect(Collectors.toSet());
            vehicle.setDrivers(driverService.getByIds(driverIds));
        }
        Vehicle vehicleRes = vehicleService.createForCompany(vehicle, company);
        return vehicleMapper.toDto(vehicleRes);
    }

    @GetMapping("/{id}")
    VehicleDto getById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getById(id);
        return vehicleMapper.toDto(vehicle);
    }

    @GetMapping("/types")
    List<VehicleTypeDto> getTypes() {
        List<VehicleType> all = vehicleTypeService.getAll();
        return all.stream().map(vehicleTypeMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping
    Page<VehicleDto> getAllVehicles(@RequestParam(required = false) String search,
                                    @RequestParam(required = false) Long filter,
                                    @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        var company = companyService.getOwnerCompany();
        Page<Vehicle> page = vehicleService.getPageByCompanyId(company, search, filter, pageable);
        return page.map(vehicleMapper::toDto);
    }

    @PutMapping("/{id}")
    VehicleDto updateVehicleById(@PathVariable Long id, @RequestBody VehicleDto vehicleDto) {
        return vehicleService.updateById(id, vehicleDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteVehicleById(@PathVariable Long id) {
        vehicleService.deleteById(id);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestPart("file") MultipartFile file) {
        Long companyId = authenticationFacade.getCurrentUser().getCompanyId();
        Company company = companyService.getCompanyById(companyId);
        vehicleService.upload(file, company);
    }

}