CREATE SEQUENCE vme.dic_vehicle_types_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE vme.dic_vehicle_types
(
    id bigint NOT NULL DEFAULT nextval('vme.dic_vehicle_types_id_seq'::regclass),
    created_at timestamp without time zone NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default",
    updated_at timestamp without time zone,
    title character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT vehicle_type_pkey PRIMARY KEY (id)
);