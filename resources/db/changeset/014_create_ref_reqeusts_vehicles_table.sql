create table vme.ref_requests_vehicles
(
    request_id bigint not null
        constraint ref_requests_vehicles_dat_requests_id_fk
            references vme.dat_requests,
    vehicle_id bigint not null
        constraint ref_requests_vehicles_dat_vehicles_id_fk
            references vme.dat_vehicles
);
