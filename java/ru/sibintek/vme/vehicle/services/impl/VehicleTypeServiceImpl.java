package ru.sibintek.vme.vehicle.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sibintek.vme.vehicle.domain.VehicleType;
import ru.sibintek.vme.vehicle.repositories.VehicleTypeRepository;
import ru.sibintek.vme.vehicle.services.VehicleTypeService;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final VehicleTypeRepository vehicleTypeRepository;

    @Override
    public List<VehicleType> getAll() {
        return vehicleTypeRepository.findAll();
    }

    @Override
    public VehicleType getByName(String title) {
        return vehicleTypeRepository.findByTitle(title).orElseThrow();
    }

    @Override
    public VehicleType getById(Long id) {
        return vehicleTypeRepository.getReferenceById(id);
    }
}
