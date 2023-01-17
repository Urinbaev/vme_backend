alter table vme.dat_drivers
    add constraint dat_drivers_dat_companies_id_fk
        foreign key (company_id) references vme.dat_companies;
