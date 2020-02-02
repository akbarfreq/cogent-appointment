package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.constants.StatusConstants;
import com.cogent.cogentappointment.client.dto.request.admin.AdminInfoRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.admin.*;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.model.Admin;
import com.cogent.cogentappointment.client.repository.custom.AdminRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AdminServiceMessages;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AdminServiceMessages.ADMIN_INFO_NOT_FOUND;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.INVALID_USERNAME_OR_HOSPITAL_CODE;
import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.AdminQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti on 7/21/19
 */
@Service
@Transactional(readOnly = true)
public class AdminRepositoryCustomImpl implements AdminRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> validateDuplicity(String username, String email, String mobileNumber,
                                            Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_ADMIN_FOR_VALIDATION)
                .setParameter(USERNAME, username)
                .setParameter(EMAIL, email)
                .setParameter(MOBILE_NUMBER, mobileNumber)
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(AdminUpdateRequestDTO updateRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_ADMIN_EXCEPT_CURRENT_ADMIN)
                .setParameter(ID, updateRequestDTO.getId())
                .setParameter(EMAIL, updateRequestDTO.getEmail())
                .setParameter(MOBILE_NUMBER, updateRequestDTO.getMobileNumber())
                .setParameter(HOSPITAL_ID, updateRequestDTO.getHospitalId());

        return query.getResultList();
    }

    @Override
    public List<AdminDropdownDTO> fetchActiveAdminsForDropDown() {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_ADMIN_FOR_DROPDOWN);

        List<AdminDropdownDTO> list = transformQueryToResultList(query, AdminDropdownDTO.class);

        if (list.isEmpty()) throw NO_ADMIN_FOUND.get();
        else return list;
    }

    @Override
    public List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_ADMIN(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AdminMinimalResponseDTO> result = transformQueryToResultList(query, AdminMinimalResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) throw NO_ADMIN_FOUND.get();
        else {
            result.get(0).setTotalItems(totalItems);
            return result;
        }
    }

    @Override
    public AdminDetailResponseDTO fetchDetailsById(Long id) {
        AdminDetailResponseDTO detailResponseDTO = fetchAdminDetailResponseDTO(id);

        if (detailResponseDTO.getHasMacBinding().equals(StatusConstants.YES))
            detailResponseDTO.setAdminMacAddressInfo(getMacAddressInfo(id));

        return detailResponseDTO;
    }


    @Override
    public Admin fetchAdminByUsernameOrEmail(String username) {
        try {
            return entityManager.createQuery(QUERY_TO_FETCH_ADMIN_BY_USERNAME_OR_EMAIL, Admin.class)
                    .setParameter(USERNAME, username)
                    .setParameter(EMAIL, username)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw ADMIN_NOT_FOUND.apply(username);
        }
    }

    @Override
    public AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_INFO)
                .setParameter(USERNAME, requestDTO.getUsername())
                .setParameter(EMAIL, requestDTO.getUsername());

        try {
            return transformQueryToSingleResult(query, AdminLoggedInInfoResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(ADMIN_INFO_NOT_FOUND);
        }
    }

    @Override
    public AdminMinDetails getAdminInfoByUsernameAndHospitalCodeAndApikey(String username, String hospitalCode, String apiKey) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_BY_USERNAME_AND_HOSPITAL_CODE)
                .setParameter(USERNAME, username)
                .setParameter(API_KEY, "7ddb7cb6-937b-4615-a202-a8c0637e4d03")
                .setParameter(HOSPITAL_CODE, hospitalCode);

        return transformQueryToSingleResult(query, AdminMinDetails.class);
    }

    @Override
    public AdminMinDetails verifyLoggedInAdmin(String username, String hospitalCode) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VERIFY_LOGGED_IN_USER)
                .setParameter(USERNAME, username)
                .setParameter(HOSPITAL_CODE, hospitalCode);
        try {
            return transformQueryToSingleResult(query, AdminMinDetails.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(INVALID_USERNAME_OR_HOSPITAL_CODE);
        }
    }

    public AdminDetailResponseDTO fetchAdminDetailResponseDTO(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_DETAIL)
                .setParameter(ID, id);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty())
            throw ADMIN_WITH_GIVEN_ID_NOT_FOUND.apply(id);

        return transformQueryToResultList(query, AdminDetailResponseDTO.class).get(0);
    }

    public List<AdminMacAddressInfoResponseDTO> getMacAddressInfo(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_FO_FETCH_MAC_ADDRESS_INFO)
                .setParameter(ID, id);

        return transformQueryToResultList(query, AdminMacAddressInfoResponseDTO.class);
    }

    private Supplier<NoContentFoundException> NO_ADMIN_FOUND = () -> new NoContentFoundException(Admin.class);

    private Function<Long, NoContentFoundException> ADMIN_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Admin.class, "id", id.toString());
    };

    private Function<String, NoContentFoundException> ADMIN_NOT_FOUND = (username) -> {
        throw new NoContentFoundException(String.format(AdminServiceMessages.ADMIN_NOT_FOUND, username),
                "username/email", username);
    };
}

