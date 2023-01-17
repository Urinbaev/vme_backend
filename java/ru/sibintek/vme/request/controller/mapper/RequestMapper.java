package ru.sibintek.vme.request.controller.mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import ru.sibintek.vme.common.util.DataUtils;
import ru.sibintek.vme.request.controller.dto.AssignTSDto;
import ru.sibintek.vme.request.controller.dto.AssignTSRequestDto;
import ru.sibintek.vme.request.controller.dto.CustomerDto;
import ru.sibintek.vme.request.controller.dto.FactDataDto;
import ru.sibintek.vme.request.controller.dto.RequestDto;
import ru.sibintek.vme.request.domain.RequestBaseEntity;
import ru.sibintek.vme.request.domain.TsDriverEntity;
import ru.sibintek.vme.vehicle.domain.Vehicle;
import ru.sibintek.vme.vehicle.dto.VehicleDto;
import ru.sibintek.vme.vehicle.mapper.VehicleMapper;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RequestMapper {
    VehicleMapper vehicleMapper = Mappers.getMapper(VehicleMapper.class);

    @Mappings({
            @Mapping(target = "status", source = "currentState.state"),
            @Mapping(target = "reason", source = "currentState.reason"),
            @Mapping(target = "additionalOptions", expression = "java(parseMap(requestBaseEntity.getAdditionalOptions()))"),
            @Mapping(target = "vehicles", expression = "java(parseVehicles(requestBaseEntity.getVehicles()))"),
            @Mapping(target = "factData", source = "requestBaseEntity", qualifiedByName = "toFactData"),
            @Mapping(target = "customer", source = "requestBaseEntity", qualifiedByName = "toCustomerDto"),
            @Mapping(target = "dateCreated", source = "createdAt")
    })
    RequestDto toDTO(RequestBaseEntity requestBaseEntity);

    @Named("toCustomerDto")
    default CustomerDto toCustomerDto(RequestBaseEntity requestBaseEntity) {
        return new CustomerDto(
                requestBaseEntity.getCustomerPhone(),
                requestBaseEntity.getCustomerOrganization(),
                requestBaseEntity.getCustomerFio(),
                requestBaseEntity.getCustomerId(),
                requestBaseEntity.isCustomerOwn()
        );
    }

    @Named("toFactData")
    FactDataDto toFactData(RequestBaseEntity requestBaseEntity);

    default Map<String, Object> parseMap(Map<String, String> additionalOptions) {
        if (CollectionUtils.isEmpty(additionalOptions)) {
            return Collections.emptyMap();
        }
        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, String> entry : additionalOptions.entrySet()) {
            result.put(entry.getKey(), DataUtils.parseData(entry.getValue()));
        }
        return result;
    }

    default Set<VehicleDto> parseVehicles(Set<TsDriverEntity> vehicles) {
        var vehicleDtoMap = new HashMap<Long, VehicleDto>();
        for (TsDriverEntity vehicle : vehicles) {
            var vehicleDto = vehicleDtoMap.getOrDefault(
                    vehicle.getVehicle().getId(),
                    vehicleMapper.toDto(vehicle.getVehicle()));

            vehicleDto
                    .getDrivers()
                    .stream()
                    .filter(d -> Objects.equals(d.getId(), vehicle.getId().getDriverId()))
                    .forEach(d -> d.setActive(true));

            vehicleDtoMap.put(vehicleDto.getId(), vehicleDto);
        }
        return new HashSet<>(vehicleDtoMap.values());
    }

    /**
     * Маппинг фактических данных внешней заявки.
     *
     * @param requestBaseEntity instance of {@link RequestBaseEntity}
     * @param factDataDto               instance of {@link FactDataDto}
     */
    void mapFactData(@MappingTarget RequestBaseEntity requestBaseEntity, FactDataDto factDataDto);

    /**
     * Маппинг {@link AssignTSDto}
     *
     * @param assignTSRequestDto {@link AssignTSDto}
     * @param vehicle            {@link Vehicle}
     * @param requestBaseEntity    {@link RequestBaseEntity}
     * @return {@link AssignTSDto}
     */
    @Mappings({
            @Mapping(target = "idTs", source = "vehicle.id"),
            @Mapping(target = "model", source = "vehicle.model"),
            @Mapping(target = "type", constant = "other_vehicle"),
            @Mapping(target = "category", constant = "other_vehicle"),
            @Mapping(target = "routeList", constant = "1"),
            @Mapping(target = "driverContactInfo", source = "assignTSRequestDto.driverContactInfo"),
            @Mapping(target = "driverFio", source = "assignTSRequestDto.driverFio"),
            @Mapping(target = "driverId", source = "assignTSRequestDto.driverId")
    })
    AssignTSDto mapAssignTs(AssignTSRequestDto assignTSRequestDto, Vehicle vehicle, RequestBaseEntity requestBaseEntity);
}
