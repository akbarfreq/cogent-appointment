package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentRoomInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public interface HospitalDepartmentRoomInfoRepository extends JpaRepository<HospitalDepartmentRoomInfo,Long>,
        HospitalDepartmentRoomInfoRepositoryCustom {

    @Query(value = "SELECT hdi.room.id FROM HospitalDepartmentRoomInfo hdi WHERE hdi.hospitalDepartment.id=:hospitalDepartmentId AND hdi.status='Y'")
    List<Long> fetchRoomIdListByHospitalDepartmentId(@Param("hospitalDepartmentId") Long hospitalDepartmentId);
}
