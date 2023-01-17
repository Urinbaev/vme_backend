package ru.sibintek.vme.vehicle.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.vehicle.domain.Vehicle;
import ru.sibintek.vme.vehicle.domain.VehicleType;

import java.util.Optional;

public interface VehicleTypeRepository extends JpaRepository<VehicleType,Long> {
    Optional<VehicleType> findByTitle(String title);
}
