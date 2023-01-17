package ru.sibintek.vme.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;
import ru.sibintek.vme.common.domain.BaseEntity;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.driver.domain.Driver;
import ru.sibintek.vme.request.domain.TsDriverEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "dat_vehicles", schema = "vme")
@Where(clause = "status = 'ACTIVE'")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Vehicle extends BaseEntity {

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "model")
    private String model;

    @Column(name = "year_of_issue")
    private Integer yearOfIssue;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    VehicleType vehicleType;

    @Column(name = "mark")
    private String mark;

    @Column(name = "vin")
    private String vin;

    @ManyToOne
    @JoinColumn(name = "company_id")
    Company company;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "ref_driver_vehicle", schema = "vme",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "driver_id"))
    private Set<Driver> drivers = new HashSet<>();

    @OneToMany(mappedBy = "request", orphanRemoval = true)
    private Set<TsDriverEntity> requests;


    public void addDriver(Driver driver) {
        for (var var1 : drivers) {
            if (Objects.equals(var1.getId(), driver.getId())) {
                return;
            }
        }

        drivers.add(driver);
        driver.getVehicles().add(this);
    }
}