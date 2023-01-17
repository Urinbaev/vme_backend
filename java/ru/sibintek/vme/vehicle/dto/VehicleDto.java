package ru.sibintek.vme.vehicle.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.sibintek.vme.driver.dto.DriverDto;

import java.util.Set;

@Data
@Getter
@Setter
public class VehicleDto {
    private Long id;
    private String regNumber;
    private String model;
    private Integer yearOfIssue;
    private Long vehicleTypeId;
    private String vin;
    private String mark;
    private Set<DriverDto> drivers;

}