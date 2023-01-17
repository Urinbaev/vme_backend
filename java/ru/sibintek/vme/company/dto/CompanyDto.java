package ru.sibintek.vme.company.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDto {

    private Long id;
    private String name;
    private String inn;
    private String kpp;
    private String ogrn;
    private String address;
    private String phone;

}