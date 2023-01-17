package ru.sibintek.vme.vehicle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import ru.sibintek.vme.vehicle.domain.Vehicle;
import ru.sibintek.vme.vehicle.domain.VehicleType;
import ru.sibintek.vme.vehicle.dto.VehicleDto;
import ru.sibintek.vme.vehicle.dto.VehicleTypeDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleTypeMapper {

    VehicleTypeDto toDto(VehicleType vehicle);


}
