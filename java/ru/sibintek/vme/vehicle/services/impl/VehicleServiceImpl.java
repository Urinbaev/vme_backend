package ru.sibintek.vme.vehicle.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sibintek.vme.common.exception.RecordNotFoundException;
import ru.sibintek.vme.common.security.AuthenticationFacade;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.driver.dto.DriverDto;
import ru.sibintek.vme.driver.service.DriverService;
import ru.sibintek.vme.vehicle.domain.Vehicle;
import ru.sibintek.vme.vehicle.domain.VehicleType;
import ru.sibintek.vme.vehicle.dto.VehicleDto;
import ru.sibintek.vme.vehicle.mapper.VehicleMapper;
import ru.sibintek.vme.vehicle.repositories.VehicleRepository;
import ru.sibintek.vme.vehicle.services.VehicleService;
import ru.sibintek.vme.vehicle.services.VehicleTypeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.sibintek.vme.common.domain.EntityStatus.INACTIVE;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverService driverService;
    private final VehicleMapper vehicleMapper;
    private final VehicleTypeService vehicleTypeService;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public Vehicle createForCompany(Vehicle vehicle, Company company) {
        vehicle.setCompany(company);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getById(Long id) {
        return vehicleRepository.getReferenceById(id);
    }

    @Override
    public Vehicle getByIdOwn(Long id) {
        Long companyId = authenticationFacade.getCurrentUser().getCompanyId();
        return vehicleRepository.findByIdAndCompanyId(id, companyId).orElseThrow(
                () -> new RecordNotFoundException(String.format("Vehicle with ID: %s was not found", id))
        );
    }

    @Override
    public Page<Vehicle> getPageByCompanyId(Company company, String search, Long filter, Pageable pageable) {
        return vehicleRepository.findAllByFilter(company, search, filter, pageable);
    }

    @Override
    public VehicleDto updateById(Long id, VehicleDto vehicleDto) {
        Vehicle vehicle = getByIdOwn(id);
        vehicle.setRegNumber(vehicleDto.getRegNumber());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setYearOfIssue(vehicleDto.getYearOfIssue());
        vehicle.setVehicleType(vehicleTypeService.getById(vehicleDto.getVehicleTypeId()));
        vehicle.setVin(vehicleDto.getVin());
        vehicle.setMark(vehicleDto.getMark());
        vehicle.setDrivers(null);
        if (Objects.nonNull(vehicleDto.getDrivers())) {
            Set<Long> driverIds = vehicleDto.getDrivers().stream().map(DriverDto::getId).collect(Collectors.toSet());
            vehicle.setDrivers(driverService.getByIds(driverIds));
        }
        return vehicleMapper.toDto(vehicleRepository.save(vehicle));
    }

    @Override
    public void deleteById(Long id) {
        Vehicle vehicle = getByIdOwn(id);
        vehicle.setStatus(INACTIVE);
        vehicleRepository.save(vehicle);
    }


    @SneakyThrows
    @Override
    public void upload(MultipartFile file, Company company) {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        int rowNumber = 0;
        List<Vehicle> res = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        for (Row row : sheetAt) {
            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }
            Vehicle vehicle = new Vehicle();
            vehicle.setCompany(company);
            vehicle.setRegNumber(formatter.formatCellValue(row.getCell(0)));
            vehicle.setModel(formatter.formatCellValue(row.getCell(1)));
            vehicle.setYearOfIssue(Integer.parseInt(formatter.formatCellValue(row.getCell(2))));
            vehicle.setVehicleType(getVehicleType(formatter.formatCellValue(row.getCell(3))));
            vehicle.setVin(formatter.formatCellValue(row.getCell(4)));
            vehicle.setMark(formatter.formatCellValue(row.getCell(5)));
            res.add(vehicle);
        }
        vehicleRepository.saveAll(res);
    }

    @Override
    public Vehicle getByIdAndType(Long id, VehicleType vehicleType) {
        return vehicleRepository.findByIdAndVehicleType(id, vehicleType)
                .orElseThrow(
                        () -> new RecordNotFoundException(String.format("Vehicle with ID: %s and Vehicle type: %s was not found", id, vehicleType.getTitle()))
                );
    }

    private VehicleType getVehicleType(String stringCellValue) {
        return vehicleTypeService.getByName(stringCellValue);
    }

}