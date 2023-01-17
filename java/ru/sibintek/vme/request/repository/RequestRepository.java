package ru.sibintek.vme.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sibintek.vme.company.domain.Company;
import ru.sibintek.vme.request.domain.RequestBaseEntity;
import ru.sibintek.vme.request.domain.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<RequestBaseEntity, Long>, JpaSpecificationExecutor<RequestBaseEntity> {

    @Query("SELECT DISTINCT re FROM RequestBaseEntity re join re.stateHistories he left join re.vehicles tsve left join tsve.vehicle ve left join ve.vehicleType vt" +
            "   WHERE (he.state IN :statues AND he.status = 'ACTIVE' )" +
            "       AND (:company IS NULL OR re.company = :company) " +
            "       AND (:search IS NULL OR :search = '' OR cast(re.number AS string) like UPPER(CONCAT(:search,'%'))" +
            "           OR UPPER(re.customerOrganization) like UPPER(CONCAT('%',:search,'%'))) " +
            "       AND (cast(:createdDateFromFilter as LocalDateTime)  IS NULL OR re.createdAt >= :createdDateFromFilter )" +
            "       AND (cast(:createdDateToFilter as LocalDateTime) IS NULL OR re.createdAt <= :createdDateToFilter)" +
            "       AND (:filter IS NULL OR vt.id = :filter)"
    )
    Page<RequestBaseEntity> getList(@Param("statues") List<RequestStatus> statues,
                                    @Param("search") String search,
                                    @Param("filter") Long filter,
                                    @Param("createdDateFromFilter") LocalDateTime createdDateFromFilter,
                                    @Param("createdDateToFilter") LocalDateTime createdDateToFilter,
                                    @Param("company") Company company, Pageable pageable);

    Optional<RequestBaseEntity> findByUidAndOrganization(String uuid, Long organization);

    Optional<RequestBaseEntity> findByUid(String uuid);
}
