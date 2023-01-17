package ru.sibintek.vme.vehicle.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.vehicle.domain.Vehicle;
import ru.sibintek.vme.vehicle.domain.VehicleType;
import ru.sibintek.vme.vehicle.dto.VehicleDto;

public interface VehicleService {

    Vehicle createForCompany(Vehicle vehicle, Company company);

    Vehicle getById(Long id);

    Vehicle getByIdOwn(Long id);

    Page<Vehicle> getPageByCompanyId(Company company, String search, Long filter, Pageable pageable);

    VehicleDto updateById(Long id, VehicleDto vehicleDto);

    void deleteById(Long id);

    void upload(MultipartFile file, Company company);

    Vehicle getByIdAndType(Long id, VehicleType vehicleType);
}
