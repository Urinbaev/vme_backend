package ru.sibintek.vme.vehicle.domain;

import lombok.Data;
import ru.sibintek.vme.common.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "dic_vehicle_types", schema = "vme")
public class VehicleType extends BaseEntity {

    String title;

}