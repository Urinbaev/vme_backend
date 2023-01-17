package ru.sibintek.vme.driver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.driver.domain.Driver;

import java.util.Optional;
import java.util.Set;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Set<Driver> findByIdIn(Set<Long> ids);

    Optional<Driver> findByIdAndCompany(Long id, Company company);

    @Query("SELECT d FROM Driver d WHERE d.company = ?1 AND " +
            "(?2 IS NULL OR ?2 = '' OR UPPER(d.fio) LIKE UPPER(CONCAT('%',?2,'%')) " +
            "OR UPPER(d.personnelNumber) LIKE UPPER(CONCAT('%',?2,'%')))")
    Page<Driver> findAllByFilter(Company company, String filter, Pageable pageable);

}
