alter table vme.dat_requests
    add column vehicle_type_id bigserial,
    add constraint dat_requests_dic_vehicle_types_id_fk
        foreign key (vehicle_type_id) references vme.dic_vehicle_types;
