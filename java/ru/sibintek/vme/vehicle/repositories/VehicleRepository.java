package ru.sibintek.vme.vehicle.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.vehicle.domain.Vehicle;
import ru.sibintek.vme.vehicle.domain.VehicleType;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("select v from Vehicle v where v.company = :company AND " +
            "   (:search IS NULL OR :search = '' OR (v.mark) LIKE (CONCAT('%',:search,'%')) OR (v.regNumber) LIKE (CONCAT('%',:search,'%')) " +
            "       OR (v.vin) LIKE (CONCAT('%',:search,'%'))) AND " +
            "   (:filter IS NULL OR (v.vehicleType.id) = (:filter))"
    )
    Page<Vehicle> findAllByFilter(@Param("company") Company company,
                                  @Param("search") String search,
                                  @Param("filter") Long filter, Pageable pageable);

    Optional<Vehicle> findByIdAndCompanyId(Long id, Long companyId);

    Optional<Vehicle> findByIdAndVehicleType(Long id, VehicleType vehicleType);
}
