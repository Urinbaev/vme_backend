package ru.sibintek.vme.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sibintek.vme.company.domain.Company;


public interface CompanyRepository extends JpaRepository<Company, Long> {

}