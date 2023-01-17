alter table vme.dat_requests
    add column customer_phone        varchar(20),
    add column customer_id           integer,
    add column customer_organization varchar(250),
    add column customer_fio          varchar,
    add column customer_own          boolean;


update vme.dat_requests
set customer_id           = cast(customer::json ->> 'id' as integer),
    customer_fio          = customer::json ->> 'fio',
    customer_organization = customer::json ->> 'organization',
    customer_phone        = customer::json ->> 'phone',
    customer_own          = cast(customer::json ->> 'own' as boolean)
where customer is not null
  and customer is not null;

alter table vme.dat_requests
    drop column customer;