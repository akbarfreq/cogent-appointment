package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DepartmentRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti ON 25/01/2020
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, DepartmentRepositoryCustom {

    @Query(value = "SELECT d FROM Department d WHERE d.id = :id AND d.status != 'D'")
    Optional<Department> findDepartmentById(@Param("id") Long id);

    @Query(value = "SELECT d FROM Department d WHERE d.id = :id AND d.status = 'Y'")
    Optional<Department> findActiveDepartmentById(@Param("id") Long id);

}
