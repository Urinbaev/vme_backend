package ru.sibintek.vme.driver.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.sibintek.vme.driver.domain.Driver;
import ru.sibintek.vme.driver.dto.DriverDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DriverMapper {

    DriverDto toDto(Driver driver);

    Driver toEntity(DriverDto driverDto);

}