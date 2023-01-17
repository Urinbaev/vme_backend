package ru.sibintek.vme.driver.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import ru.sibintek.vme.common.domain.BaseEntity;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.vehicle.domain.Vehicle;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "dat_drivers", schema = "vme")
@Where(clause = "status = 'ACTIVE'")
public class Driver extends BaseEntity {

    @EqualsAndHashCode.Include
    @Column(name = "personnel_number")
    private String personnelNumber;

    @EqualsAndHashCode.Include
    @Column(name = "fio")
    private String fio;

    @Column(name = "contact_info")
    @EqualsAndHashCode.Include
    private String contactInfo;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(mappedBy = "drivers")
    private Set<Vehicle> vehicles = new HashSet<>();

}