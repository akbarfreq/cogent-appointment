package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Transactional(readOnly = true)
public interface HospitalDepartmentRepository extends JpaRepository<HospitalDepartment, Long>,
        HospitalDepartmentRepositoryCustom {

    @Query(value = "SELECT sd FROM HospitalDepartment sd WHERE sd.id = :id AND sd.hospital.id= :hospitalId" +
            " AND sd.status != 'D'")
    Optional<HospitalDepartment> fetchByIdAndHospitalId(@Param("id") Long id, @Param("hospitalId") Long hospitalId);
}
