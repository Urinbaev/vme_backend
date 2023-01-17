
CREATE SEQUENCE vme.dat_vehicles_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE vme.dat_vehicles
(
    id bigint NOT NULL DEFAULT nextval('vme.dat_vehicles_id_seq'::regclass),
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default",
    updated_at timestamp without time zone,
    model character varying(255) COLLATE pg_catalog."default",
    reg_number character varying(255) COLLATE pg_catalog."default",
    year_of_issue integer,
    company_id bigint,
    vehicle_type_id bigint,
    CONSTRAINT vehicle_pkey PRIMARY KEY (id),
    CONSTRAINT fkddtxlc05rojc56bprvek17hnk FOREIGN KEY (vehicle_type_id)
        REFERENCES vme.dic_vehicle_types (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkmor1riiahy5i9lm7hfm55ht1b FOREIGN KEY (company_id)
        REFERENCES vme.dat_companies (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);