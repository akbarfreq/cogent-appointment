package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.ClientFeatureIntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author rupak on 2020-05-19
 */
@Repository
public interface ClientFeatureIntegrationRepository extends JpaRepository<ClientFeatureIntegration,Long>,
        ClientFeatureIntegrationRepositoryCustom {

    @Query("SELECT cfi from ClientFeatureIntegration cfi where cfi.id=:id AND cfi.status!='D'")
    Optional<ClientFeatureIntegration> findClientFeatureIntegrationById(@Param("id") Long id);

    @Query("SELECT cfi from ClientFeatureIntegration cfi where cfi.hospitalId=:hospitalid AND cfi.featureId=:featureid AND cfi.status!='D'")
    ClientFeatureIntegration findByHospitalWiseFeatureId(@Param("hospitalid") Long hospitalId,@Param("featureid") Long featureTypeId);
}
