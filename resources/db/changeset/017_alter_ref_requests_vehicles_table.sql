alter table vme.ref_requests_vehicles
    add driver_id bigint;

delete from vme.ref_requests_vehicles where true;

alter table vme.ref_requests_vehicles
    alter column driver_id set not null;

alter table vme.ref_requests_vehicles
    add constraint ref_requests_vehicles_dat_drivers_id_fk
        foreign key (driver_id) references vme.dat_drivers;

alter table vme.ref_requests_vehicles
    add constraint ref_requests_vehicles_pk
        primary key (request_id, vehicle_id, driver_id);

