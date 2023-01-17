create table vme.dat_companies
(
    id               bigserial,
    name             varchar(1000),
    inn              varchar(12),
    kpp              varchar(9),
    ogrn             varchar(20),
    address          varchar(2000),
    phone            varchar(20),
    created_by       varchar(255),
    last_modified_by varchar(255),
    created_at       timestamp without time zone NOT NULL,
    updated_at       timestamp without time zone,
    status           varchar(10),
    constraint pk_dat_companies primary key (id)
);
