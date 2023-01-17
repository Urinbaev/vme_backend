package ru.sibintek.vme.company.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import ru.sibintek.vme.common.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "dat_companies", schema = "vme")
@Where(clause = "status = 'ACTIVE'")
public class Company extends BaseEntity {

    @EqualsAndHashCode.Include
    @Column(name = "name")
    private String name;

    @EqualsAndHashCode.Include
    @Column(name = "inn")
    private String inn;

    @EqualsAndHashCode.Include
    @Column(name = "kpp")
    private String kpp;

    @EqualsAndHashCode.Include
    @Column(name = "ogrn")
    private String ogrn;

    @EqualsAndHashCode.Include
    @Column(name = "address")
    private String address;

    @EqualsAndHashCode.Include
    @Column(name = "phone")
    private String phone;

}