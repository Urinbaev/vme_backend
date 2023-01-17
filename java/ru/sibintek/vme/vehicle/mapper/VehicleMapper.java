package ru.sibintek.vme.vehicle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import ru.sibintek.vme.vehicle.domain.Vehicle;
import ru.sibintek.vme.vehicle.dto.VehicleDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

    @Mappings({@Mapping(target = "vehicleTypeId", source = "vehicleType.id")})
    VehicleDto toDto(Vehicle vehicle);

    @Mappings({
            @Mapping(target = "vehicleType.id", source = "vehicleTypeId"),
            @Mapping(target = "drivers", ignore = true)
    })
    Vehicle toEntity(VehicleDto vehicleDto);

}