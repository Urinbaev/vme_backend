package ru.sibintek.vme.driver.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.driver.domain.Driver;
import ru.sibintek.vme.driver.dto.DriverDto;

import java.io.InputStream;
import java.util.Set;

public interface DriverService {

    Driver addDriver(Long companyId, DriverDto driverDto);

    DriverDto getDriver(Long id);

    Page<DriverDto> getDrivers(Long companyId, String filter, Pageable pageable);

    DriverDto updateDriver(Long id, Long companyId, DriverDto driverDto);

    void deleteDriver(Long id, Long companyId);

    Driver getDriverById(Long driverId);

    Set<Driver> getByIds(Set<Long> ids);

    void upload(InputStream inputStream, Company company);
}