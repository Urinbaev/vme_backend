package ru.sibintek.vme.vehicle.services;

import ru.sibintek.vme.vehicle.domain.VehicleType;

import java.util.List;

public interface VehicleTypeService {
    List<VehicleType> getAll();

    VehicleType getByName(String stringCellValue);

    VehicleType getById(Long id);
}
