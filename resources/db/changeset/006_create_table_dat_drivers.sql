create table vme.dat_drivers
(
    id               bigserial,
    personnel_number varchar(20),
    name             varchar(1000),
    surname          varchar(1000),
    phone            varchar(20),
    company_id       bigserial,
    created_by       varchar(255),
    last_modified_by varchar(255),
    created_at       timestamp without time zone NOT NULL,
    updated_at       timestamp without time zone,
    status           varchar(10),
        constraint dat_drivers_pk
            primary key (id)
);
