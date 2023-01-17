package ru.sibintek.vme.driver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sibintek.vme.common.domain.EntityStatus;
import ru.sibintek.vme.common.exception.RecordNotFoundException;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.company.repository.CompanyRepository;
import ru.sibintek.vme.driver.domain.Driver;
import ru.sibintek.vme.driver.dto.DriverDto;
import ru.sibintek.vme.driver.mapper.DriverMapper;
import ru.sibintek.vme.driver.repository.DriverRepository;
import ru.sibintek.vme.driver.service.DriverService;

import javax.persistence.EntityNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public Driver addDriver(Long companyId, DriverDto driverDto) {
        Company company = companyRepository.getReferenceById(companyId);
        Driver driver = driverMapper.toEntity(driverDto);
        driver.setCompany(company);
        return driverRepository.save(driver);
    }

    @Override
    @Transactional(readOnly = true)
    public DriverDto getDriver(Long id) {
        return driverMapper.toDto(getDriverById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriverDto> getDrivers(Long companyId, String filter, Pageable pageable) {
        Company company = companyRepository.getReferenceById(companyId);
        return driverRepository.findAllByFilter(company, filter, pageable).map(driverMapper::toDto);
    }

    @Override
    @Transactional
    public DriverDto updateDriver(Long id, Long companyId, DriverDto driverDto) {
        Company company = companyRepository.getReferenceById(companyId);
        Driver driver = driverRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Driver with ID: %s was not found", id)));

        driver.setPersonnelNumber(driverDto.getPersonnelNumber());
        driver.setFio(driverDto.getFio());
        driver.setContactInfo(driverDto.getContactInfo());
        Driver saved = driverRepository.save(driver);
        return driverMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteDriver(Long id, Long companyId) {
        Company company = companyRepository.getReferenceById(companyId);
        Driver driver = driverRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Driver with ID: %s was not found", id)));

        driver.setStatus(EntityStatus.INACTIVE);
        driverRepository.save(driver);
    }

    @Override
    @Transactional(readOnly = true)
    public Driver getDriverById(Long driverId) {
        try {
            return driverRepository.getReferenceById(driverId);
        } catch (EntityNotFoundException e) {
            throw new RecordNotFoundException(String.format("Driver with ID: %s was not found", driverId));
        }
    }

    @Override
    public Set<Driver> getByIds(Set<Long> ids) {
        return driverRepository.findByIdIn(ids);
    }

    @SneakyThrows
    @Override
    public void upload(InputStream inputStream, Company company) {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        int rowNumber = 0;
        List<Driver> res = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        for (Row row : sheetAt) {
            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }
            Driver driver = new Driver();
            driver.setCompany(company);
            driver.setFio(formatter.formatCellValue(row.getCell(0)));
            driver.setPersonnelNumber(formatter.formatCellValue(row.getCell(1)));
            driver.setContactInfo(formatter.formatCellValue(row.getCell(2)));
            res.add(driver);
        }
        driverRepository.saveAll(res);
    }

}