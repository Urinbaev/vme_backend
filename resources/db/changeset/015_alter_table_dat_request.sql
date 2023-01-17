alter table vme.dat_requests
    add column company_id bigserial,
    add constraint dat_requests_dat_companies_id_fk
        foreign key (company_id) references vme.dat_companies;
