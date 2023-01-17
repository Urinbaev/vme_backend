create table vme.ref_driver_vehicle
(
    driver_id               bigserial,
    constraint dat_driver_vehicle_dat_drivers_id_fk
        foreign key (driver_id) references vme.dat_drivers,
    vehicle_id              bigint,
        constraint dat_driver_vehicle_dat_vehicles_id_fk
        foreign key (vehicle_id) references vme.dat_vehicles,
            constraint dat_driver_vehicle_pk
            primary key (driver_id, vehicle_id)

);


