package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Qualification;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;
import com.cogent.cogentappointment.persistence.model.University;

import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;


/**
 * @author smriti on 11/11/2019
 */
public class QualificationUtils {

    public static Qualification parseToQualification(QualificationRequestDTO requestDTO,
                                                     University university,
                                                     QualificationAlias qualificationAlias,
                                                     Hospital hospital) {

        Qualification qualification = new Qualification();
        qualification.setName(toNormalCase(requestDTO.getName()));
        qualification.setStatus(requestDTO.getStatus());
        parseToQualification(qualification, qualificationAlias, university, hospital);
        return qualification;
    }

    private static void parseToQualification(Qualification qualification,
                                             QualificationAlias qualificationAlias,
                                             University university,
                                             Hospital hospital) {
        qualification.setQualificationAlias(qualificationAlias);
        qualification.setUniversity(university);
    }

    public static void parseToUpdatedQualification(QualificationUpdateRequestDTO requestDTO,
                                                   QualificationAlias qualificationAlias,
                                                   University university,
                                                   Qualification qualification,
                                                   Hospital hospital) {

        qualification.setName(toNormalCase(requestDTO.getName()));
        qualification.setStatus(requestDTO.getStatus());
        qualification.setRemarks(requestDTO.getRemarks());
        parseToQualification(qualification, qualificationAlias, university, hospital);
    }

    public static void parseToDeletedQualification(Qualification qualification,
                                                   DeleteRequestDTO deleteRequestDTO) {
        qualification.setStatus(deleteRequestDTO.getStatus());
        qualification.setRemarks(deleteRequestDTO.getRemarks());
    }
}
