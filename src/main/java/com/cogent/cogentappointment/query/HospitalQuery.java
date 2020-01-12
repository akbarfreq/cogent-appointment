package com.cogent.cogentappointment.query;

import com.cogent.cogentappointment.dto.request.hospital.HospitalSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author Rupak
 */
public class HospitalQuery {

    public static final String QUERY_TO_FIND_HOSPITAL_COUNT_BY_NAME =
            "SELECT COUNT(h.id) FROM Hospital h WHERE h.name =:name AND h.status != 'D'";

    public static final String QUERY_TO_FIND_HOSPITAL_COUNT_BY_ID_AND_NAME =
            "SELECT COUNT(h.id) FROM Hospital h WHERE h.id!= :id AND h.name =:name AND h.status != 'D'";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOR_DROPDOWN =
            " SELECT" +
                    " h.id as value," +                     //[0]
                    " h.name as label" +                    //[1]
                    " FROM" +
                    " Hospital h" +
                    " WHERE h.status ='Y'";

    public static String QUERY_TO_SEARCH_HOSPITAL(HospitalSearchRequestDTO searchRequestDTO) {
        return "SELECT" +
                " h.id as id," +                                //[0]
                " h.name as name," +                            //[1]
                " h.status as status," +                        //[2]
                " h.address as address," +                      //[3]
                " tbl.file_uri as fileUri" +                    //[4]
                " FROM" +
                " hospital h" +
                " LEFT JOIN" +
                " (" +
                " SELECT" +
                " hl.hospital_id as hospitalId," +
                " hl.file_uri FROM hospital_logo hl" +
                " WHERE hl.status = 'Y'" +
                " )tbl ON tbl.hospitalId= h.id" +
                GET_WHERE_CLAUSE_FOR_SEARCHING_HOSPITAL.apply(searchRequestDTO);
    }

    private static Function<HospitalSearchRequestDTO, String> GET_WHERE_CLAUSE_FOR_SEARCHING_HOSPITAL =
            (searchRequestDTO) -> {
                String whereClause = " WHERE h.status!='D'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
                    whereClause += " AND h.status='" + searchRequestDTO.getStatus() + "'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getName()))
                    whereClause += " AND h.name LIKE '%" + searchRequestDTO.getName() + "%'";

                whereClause += " ORDER BY h.id DESC";

                return whereClause;
            };

    public static final String QUERY_TO_FETCH_HOSPITAL_DETAILS =
            "SELECT" +
                    " h.id as id," +                                //[0]
                    " h.name as name," +                            //[1]
                    " h.status as status," +                        //[2]
                    " h.address as address," +                      //[3]
                    " h.pan_number as panNumber," +                  //[4]
                    " h.remarks as remarks," +                      //[5]
                    " tbl1.file_uri as fileUri," +                  //[6]
                    " tbl2.contact_details as contact_details" +    //[7]
                    " FROM" +
                    " hospital h" +
                    " LEFT JOIN" +
                    " (" +
                    " SELECT" +
                    " hl.hospital_id as hospitalId," +
                    " hl.file_uri FROM hospital_logo hl" +
                    " WHERE hl.status = 'Y'" +
                    " )tbl1 ON tbl1.hospitalId= h.id" +
                    " LEFT JOIN " +
                    " (" +
                    " SELECT hc.hospital_id as hospitalId," +
                    " GROUP_CONCAT((CONCAT(hc.id, '-', hc.contact_number))) as contact_details" +
                    " FROM hospital_contact_number hc" +
                    " WHERE hc.status = 'Y'" +
                    " GROUP by hc.hospital_id" +
                    " )tbl2 ON tbl2.hospitalId = h.id" +
                    " WHERE h.id =:id" +
                    " AND h.status !='D'";
}
