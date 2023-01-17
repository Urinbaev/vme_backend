alter table vme.dat_drivers rename column name to fio;

update vme.dat_drivers set fio = concat(fio, surname);

alter table vme.dat_drivers drop column surname;

