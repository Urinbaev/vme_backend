create table vme.dat_request_histories
(
    id                      bigserial
        constraint dat_request_histories_pk primary key,
    request_id              bigint not null
        constraint table_name_dat_requests_id_fk references vme.dat_requests,
    state                   varchar(50)  not null,
    reason                  varchar(2000),
    created_by              varchar(255),
    last_modified_by        varchar(255),
    created_at              timestamp   not null,
    updated_at              timestamp,
    status                  varchar(40)
);

create unique index dat_request_histories_id_uindex
    on vme.dat_request_histories (id);

insert into vme.dat_request_histories (request_id, state, created_at, status)
select r.id, r.state, current_date as created_at, 'ACTIVE' as status from vme.dat_requests r;

alter table vme.dat_requests
    drop column state,
    drop column previous_state;