package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.admin.*;
import com.cogent.cogentappointment.client.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminLoggedInInfoResponseDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.AdminService;
import com.cogent.cogentappointment.client.service.EmailService;
import com.cogent.cogentappointment.client.service.MinioFileService;
import com.cogent.cogentappointment.client.service.ProfileService;
import com.cogent.cogentappointment.client.validator.LoginValidator;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AdminServiceMessages.*;
import static com.cogent.cogentappointment.client.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AdminLog.*;
import static com.cogent.cogentappointment.client.utils.AdminUtils.*;
import static com.cogent.cogentappointment.client.utils.DashboardFeatureUtils.parseToAdminDashboardFeature;
import static com.cogent.cogentappointment.client.utils.GenderUtils.fetchGenderByCode;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;
import static java.lang.reflect.Array.get;

/**
 * @author smriti on 2019-08-05
 */
@Service
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final Validator validator;

    private final AdminRepository adminRepository;

    private final AdminMacAddressInfoRepository adminMacAddressInfoRepository;

    private final AdminMetaInfoRepository adminMetaInfoRepository;

    private final AdminAvatarRepository adminAvatarRepository;

    private final AdminConfirmationTokenRepository confirmationTokenRepository;

    private final DashboardFeatureRepository dashboardFeatureRepository;

    private final AdminDashboardFeatureRepository adminDashboardFeatureRepository;

    private final MinioFileService minioFileService;

    private final EmailService emailService;

    private final ProfileService profileService;

    public AdminServiceImpl(Validator validator,
                            AdminRepository adminRepository,
                            AdminMacAddressInfoRepository adminMacAddressInfoRepository,
                            AdminMetaInfoRepository adminMetaInfoRepository,
                            AdminAvatarRepository adminAvatarRepository,
                            AdminConfirmationTokenRepository confirmationTokenRepository,
                            DashboardFeatureRepository dashboardFeatureRepository,
                            AdminDashboardFeatureRepository adminDashboardFeatureRepository,
                            MinioFileService minioFileService,
                            EmailService emailService,
                            ProfileService profileService) {
        this.validator = validator;
        this.adminRepository = adminRepository;
        this.adminMacAddressInfoRepository = adminMacAddressInfoRepository;
        this.adminMetaInfoRepository = adminMetaInfoRepository;
        this.adminAvatarRepository = adminAvatarRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.dashboardFeatureRepository = dashboardFeatureRepository;
        this.adminDashboardFeatureRepository = adminDashboardFeatureRepository;
        this.minioFileService = minioFileService;
        this.emailService = emailService;
        this.profileService = profileService;
    }

    @Override
    public void save(@Valid AdminRequestDTO adminRequestDTO, MultipartFile files) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, ADMIN);

        Long hospitalId = getLoggedInHospitalId();

        validateConstraintViolation(validator.validate(adminRequestDTO));

        validateAdminCount(hospitalId);

        List<Object[]> admins = adminRepository.validateDuplicity(adminRequestDTO.getUsername(),
                adminRequestDTO.getEmail(), adminRequestDTO.getMobileNumber(), hospitalId);

        validateAdminDuplicity(admins, adminRequestDTO.getUsername(), adminRequestDTO.getEmail(),
                adminRequestDTO.getMobileNumber());

        Admin admin = save(adminRequestDTO, hospitalId);

        saveAdminAvatar(admin, files);

        saveMacAddressInfo(admin, adminRequestDTO.getMacAddressInfo());

        saveAdminMetaInfo(admin);

        saveAllAdminDashboardFeature(adminRequestDTO.getAdminDashboardRequestDTOS(), admin);

        AdminConfirmationToken adminConfirmationToken =
                saveAdminConfirmationToken(parseInAdminConfirmationToken(admin));

        EmailRequestDTO emailRequestDTO = convertAdminRequestToEmailRequestDTO(adminRequestDTO,
                adminConfirmationToken.getConfirmationToken());

        sendEmail(emailRequestDTO);

        log.info(SAVING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinAdmin() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, ADMIN);

        List<DropDownResponseDTO> activeMinAdmin = adminRepository.fetchActiveMinAdmin(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return activeMinAdmin;
    }

    @Override
    public List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, ADMIN);

        List<AdminMinimalResponseDTO> responseDTOS =
                adminRepository.search(searchRequestDTO, getLoggedInHospitalId(), pageable);

        log.info(SEARCHING_PROCESS_STARTED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AdminDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, ADMIN);

        AdminDetailResponseDTO responseDTO = adminRepository.fetchDetailsById(id, getLoggedInHospitalId());

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, ADMIN);

        Admin admin = findAdminByIdAndHospitalId(deleteRequestDTO.getId(), getLoggedInHospitalId());

        convertAdminToDeleted(admin, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void changePassword(AdminChangePasswordRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PASSWORD_PROCESS_STARTED);

        Admin admin = findAdminByIdAndHospitalId(requestDTO.getId(), getLoggedInHospitalId());

        validatePassword(admin, requestDTO);

        updateAdminPassword(requestDTO.getNewPassword(), requestDTO.getRemarks(), admin);

        log.info(UPDATING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void resetPassword(AdminResetPasswordRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PASSWORD_PROCESS_STARTED);

        Admin admin = findByUsername(requestDTO.getUsername());

        updateAdminPassword(requestDTO.getPassword(), requestDTO.getRemarks(), admin);

        sendEmail(parseToResetPasswordEmailRequestDTO(requestDTO, admin.getEmail()));

        log.info(UPDATING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updateAvatar(MultipartFile files, Long adminId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ADMIN_AVATAR);

        Admin admin = findAdminByIdAndHospitalId(adminId, getLoggedInHospitalId());

        updateAvatar(admin, files);

        log.info(UPDATING_PROCESS_STARTED, ADMIN_AVATAR, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(@Valid AdminUpdateRequestDTO updateRequestDTO, MultipartFile files) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ADMIN);

        validateConstraintViolation(validator.validate(updateRequestDTO));

        Long hospitalId = getLoggedInHospitalId();

        Admin admin = findAdminByIdAndHospitalId(updateRequestDTO.getId(), hospitalId);

        List<Object[]> admins = adminRepository.validateDuplicity(updateRequestDTO, hospitalId);

        validateAdminDuplicity(admins, updateRequestDTO.getEmail(), updateRequestDTO.getMobileNumber());

        EmailRequestDTO emailRequestDTO = parseUpdatedInfo(updateRequestDTO, admin);

        update(updateRequestDTO, admin, hospitalId);

        if (updateRequestDTO.getIsAvatarUpdate().equals(YES))
            updateAvatar(admin, files);

        updateMacAddressInfo(updateRequestDTO.getMacAddressUpdateInfo(), admin);

        updateAdminDashboardFeature(updateRequestDTO.getAdminDashboardRequestDTOS(), admin);

        updateAdminMetaInfo(admin);

        sendEmail(emailRequestDTO);

        log.info(UPDATING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void verifyConfirmationToken(String token) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(VERIFY_CONFIRMATION_TOKEN_PROCESS_STARTED);

        Object status = confirmationTokenRepository.findByConfirmationToken(token);

        validateStatus(status);

        log.info(VERIFY_CONFIRMATION_TOKEN_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void savePassword(AdminPasswordRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PASSWORD_PROCESS_STARTED);

        AdminConfirmationToken adminConfirmationToken =
                confirmationTokenRepository.findAdminConfirmationTokenByToken(requestDTO.getToken())
                        .orElseThrow(() -> CONFIRMATION_TOKEN_NOT_FOUND.apply(requestDTO.getToken()));

        save(saveAdminPassword(requestDTO, adminConfirmationToken));
        adminConfirmationToken.setStatus(INACTIVE);

        log.info(SAVING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN);

        AdminLoggedInInfoResponseDTO responseDTO =
                adminRepository.fetchLoggedInAdminInfo(requestDTO, getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<AdminMetaInfoResponseDTO> fetchAdminMetaInfo() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN_META_INFO);

        List<AdminMetaInfoResponseDTO> metaInfoResponseDTOS =
                adminMetaInfoRepository.fetchAdminMetaInfo(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_COMPLETED, ADMIN_META_INFO, getDifferenceBetweenTwoTime(startTime));

        return metaInfoResponseDTOS;
    }

    private void updateAdminDashboardFeature(List<AdminDashboardRequestDTO> adminDashboardRequestDTOS, Admin admin) {

        List<AdminDashboardFeature> adminDashboardFeatureList = new ArrayList<>();
        adminDashboardRequestDTOS.forEach(result -> {

            AdminDashboardFeature adminDashboardFeature = adminDashboardFeatureRepository.findAdminDashboardFeatureBydashboardFeatureId(result.getId(), admin.getId());

            if (adminDashboardFeature == null) {
                saveAdminDashboardFeature(result.getId(), admin);
            }

            if (adminDashboardFeature != null) {
                adminDashboardFeature.setStatus(result.getStatus());
                adminDashboardFeatureList.add(adminDashboardFeature);
            }

        });

        adminDashboardFeatureRepository.saveAll(adminDashboardFeatureList);

    }

    public void saveAdminDashboardFeature(Long id, Admin admin) {

        DashboardFeature dashboardFeature = dashboardFeatureRepository.findActiveDashboardFeatureById(id)
                .orElseThrow(() -> new NoContentFoundException(DashboardFeature.class));

        List<DashboardFeature> dashboardFeatureList = Arrays.asList(dashboardFeature);
        adminDashboardFeatureRepository.saveAll(parseToAdminDashboardFeature(dashboardFeatureList, admin));

    }




    private void saveAllAdminDashboardFeature(List<AdminDashboardRequestDTO> dashboardRequestDTOList, Admin admin) {

        if (dashboardRequestDTOList.size() > 0) {
            List<DashboardFeature> dashboardFeatureList = findActiveDashboardFeatures(dashboardRequestDTOList);
            adminDashboardFeatureRepository.saveAll(parseToAdminDashboardFeature(dashboardFeatureList, admin));
        }

    }

    private List<DashboardFeature> findActiveDashboardFeatures(List<AdminDashboardRequestDTO> adminDashboardRequestDTOS) {

        String ids = adminDashboardRequestDTOS.stream()
                .map(request -> request.getId().toString())
                .collect(Collectors.joining(","));

        List<DashboardFeature> dashboardFeatureList = validateDashboardFeature(ids);
        int requestCount = adminDashboardRequestDTOS.size();

        if ((dashboardFeatureList.size()) != requestCount) {
            throw new NoContentFoundException(DashboardFeature.class);
        }

        return dashboardFeatureList;

    }

    private List<DashboardFeature> validateDashboardFeature(String ids) {
        return dashboardFeatureRepository.validateDashboardFeatureCount(ids);
    }


    private void validateStatus(Object status) {
        if (status.equals(INACTIVE)) throw ADMIN_ALREADY_REGISTERED.get();
    }

    /*CAN SAVE NUMBER OF ADMINS BASED ON HOSPITAL*/
    private void validateAdminCount(Long hospitalId) {

        Object[] objects = adminRepository.validateAdminCount(hospitalId);
        Long savedAdmin = (Long) get(objects, 0);
        Integer numberOfAdminsAllowed = (Integer) get(objects, 1);

        if (savedAdmin.intValue() == numberOfAdminsAllowed)
            throw new BadRequestException(ADMIN_CANNOT_BE_REGISTERED_MESSAGE, ADMIN_CANNOT_BE_REGISTERED_DEBUG_MESSAGE);
    }

    private void validateAdminDuplicity(List<Object[]> adminList, String requestUsername, String requestEmail,
                                        String requestMobileNumber) {

        final int USERNAME = 0;
        final int EMAIL = 1;
        final int MOBILE_NUMBER = 2;

        adminList.forEach(admin -> {
            boolean isUsernameExists = requestUsername.equalsIgnoreCase((String) get(admin, USERNAME));
            boolean isEmailExists = requestEmail.equalsIgnoreCase((String) get(admin, EMAIL));
            boolean isMobileNumberExists = requestMobileNumber.equalsIgnoreCase((String) get(admin, MOBILE_NUMBER));

            if (isUsernameExists && isEmailExists && isMobileNumberExists)
                throw ADMIN_DUPLICATION.get();

            validateUsername(isUsernameExists, requestUsername);
            validateEmail(isEmailExists, requestEmail);
            validateMobileNumber(isMobileNumberExists, requestMobileNumber);
        });
    }

    private void validateUsername(boolean isUsernameExists, String username) {
        if (isUsernameExists)
            throw new DataDuplicationException(
                    String.format(USERNAME_DUPLICATION_MESSAGE, Admin.class.getSimpleName(), username));
    }

    private void validateEmail(boolean isEmailExists, String email) {
        if (isEmailExists)
            throw new DataDuplicationException(
                    String.format(EMAIL_DUPLICATION_MESSAGE, Admin.class.getSimpleName(), email));
    }

    private void validateMobileNumber(boolean isMobileNumberExists, String mobileNumber) {
        if (isMobileNumberExists)
            throw new DataDuplicationException(
                    String.format(MOBILE_NUMBER_DUPLICATION_MESSAGE, Admin.class.getSimpleName(), mobileNumber));
    }

    private Admin save(AdminRequestDTO adminRequestDTO, Long hospitalId) {
        Gender gender = fetchGender(adminRequestDTO.getGenderCode());

        Profile profile = fetchProfile(adminRequestDTO.getProfileId(), hospitalId);

        Admin admin = parseAdminDetails(adminRequestDTO, gender, profile);

        return save(admin);
    }

    private void saveAdminAvatar(Admin admin, MultipartFile files) {
        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(admin, new MultipartFile[]{files});
            saveAdminAvatar(convertFileToAdminAvatar(responseList.get(0), admin));
        }
    }

    private List<FileUploadResponseDTO> uploadFiles(Admin admin, MultipartFile[] files) {
        String subDirectory = admin.getUsername();
        return minioFileService.addAttachmentIntoSubDirectory(subDirectory, files);
    }

    private void updateAdminAvatar(Admin admin, AdminAvatar adminAvatar, MultipartFile files) {
        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(admin, new MultipartFile[]{files});
            setFileProperties(responseList.get(0), adminAvatar);
        } else adminAvatar.setStatus(INACTIVE);

        saveAdminAvatar(adminAvatar);
    }

    private void saveAdminAvatar(AdminAvatar adminAvatar) {
        adminAvatarRepository.save(adminAvatar);
    }

    private void saveMacAddressInfo(Admin admin, List<String> macAddresses) {
        if (admin.getHasMacBinding().equals(YES)) {
            validateMacAddressInfoSize.accept(macAddresses);

            List<AdminMacAddressInfo> adminMacAddressInfos = macAddresses
                    .stream().map(macAddress -> convertToMACAddressInfo(macAddress, admin))
                    .collect(Collectors.toList());

            saveMacAddressInfo(adminMacAddressInfos);
        }
    }

    private Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    private void saveMacAddressInfo(List<AdminMacAddressInfo> adminMacAddressInfos) {
        adminMacAddressInfoRepository.saveAll(adminMacAddressInfos);
    }

    private void saveAdminMetaInfo(Admin admin) {
        adminMetaInfoRepository.save(parseInAdminMetaInfo(admin));
    }

    private void updateAdminMetaInfo(Admin admin) {
        AdminMetaInfo adminMetaInfo = adminMetaInfoRepository.findAdminMetaInfoByAdminId(admin.getId())
                .orElseThrow(() -> new NoContentFoundException(AdminMetaInfo.class));

        parseMetaInfo(admin, adminMetaInfo);
    }

    private AdminConfirmationToken saveAdminConfirmationToken(AdminConfirmationToken adminConfirmationToken) {
        return confirmationTokenRepository.save(adminConfirmationToken);
    }

    private void sendEmail(EmailRequestDTO emailRequestDTO) {
        emailService.sendEmail(emailRequestDTO);
    }

    private Admin findAdminByIdAndHospitalId(Long adminId, Long hospitalId) {
        return adminRepository.findAdminByIdAndHospitalId(adminId, hospitalId)
                .orElseThrow(() -> ADMIN_WITH_GIVEN_ID_NOT_FOUND.apply(adminId));
    }

    private Admin findByUsername(String username) {
        return adminRepository.findAdminByUsernameAndHospitalId(username, getLoggedInHospitalId())
                .orElseThrow(() -> new NoContentFoundException(Admin.class));
    }

    private void validateAdminDuplicity(List<Object[]> adminList, String requestEmail,
                                        String requestMobileNumber) {

        final int EMAIL = 0;
        final int MOBILE_NUMBER = 1;

        adminList.forEach(admin -> {
            boolean isEmailExists = requestEmail.equalsIgnoreCase((String) get(admin, EMAIL));
            boolean isMobileNumberExists = requestMobileNumber.equalsIgnoreCase((String) get(admin, MOBILE_NUMBER));

            if (isEmailExists && isMobileNumberExists)
                throw ADMIN_DUPLICATION.get();

            validateEmail(isEmailExists, requestEmail);
            validateMobileNumber(isMobileNumberExists, requestMobileNumber);
        });
    }

    private void updateAvatar(Admin admin, MultipartFile files) {
        AdminAvatar adminAvatar = adminAvatarRepository.findAdminAvatarByAdminId(admin.getId());

        if (Objects.isNull(adminAvatar)) saveAdminAvatar(admin, files);
        else updateAdminAvatar(admin, adminAvatar, files);
    }

    public void update(AdminUpdateRequestDTO adminRequestDTO, Admin admin, Long hospitalId) {
        Gender gender = fetchGender(adminRequestDTO.getGenderCode());

        Profile profile = fetchProfile(adminRequestDTO.getProfileId(), hospitalId);

        convertAdminUpdateRequestDTOToAdmin(admin, adminRequestDTO, gender, profile);
    }

    private void updateMacAddressInfo(List<AdminMacAddressInfoUpdateRequestDTO> adminMacAddressInfoUpdateRequestDTOS,
                                      Admin admin) {

        List<AdminMacAddressInfo> adminMacAddressInfos = convertToUpdatedMACAddressInfo(
                adminMacAddressInfoUpdateRequestDTOS, admin);

        saveMacAddressInfo(adminMacAddressInfos);
    }

    private EmailRequestDTO parseUpdatedInfo(AdminUpdateRequestDTO adminRequestDTO, Admin admin) {
        return parseToEmailRequestDTO(admin.getUsername(), adminRequestDTO,
                parseUpdatedValues(admin, adminRequestDTO), parseUpdatedMacAddress(adminRequestDTO));
    }

    private Consumer<List<String>> validateMacAddressInfoSize = (macInfos) -> {
        if (ObjectUtils.isEmpty(macInfos))
            throw new NoContentFoundException(AdminMacAddressInfo.class);
    };

    private void validatePassword(Admin admin, AdminChangePasswordRequestDTO requestDTO) {

        if (!LoginValidator.checkPassword(requestDTO.getOldPassword(), admin.getPassword()))
            throw new OperationUnsuccessfulException(PASSWORD_MISMATCH_MESSAGE);

        if (LoginValidator.checkPassword(requestDTO.getNewPassword(), admin.getPassword()))
            throw new DataDuplicationException(DUPLICATE_PASSWORD_MESSAGE);
    }

    private Supplier<DataDuplicationException> ADMIN_DUPLICATION = () ->
            new DataDuplicationException(ADMIN_DUPLICATION_MESSAGE);

    private Function<Long, NoContentFoundException> ADMIN_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Admin.class, "id", id.toString());
    };

    private Supplier<BadRequestException> ADMIN_ALREADY_REGISTERED = () -> new BadRequestException(ADMIN_REGISTERED);

    private Function<String, NoContentFoundException> CONFIRMATION_TOKEN_NOT_FOUND = (confirmationToken) -> {
        throw new NoContentFoundException(INVALID_CONFIRMATION_TOKEN, "confirmationToken", confirmationToken);
    };

    private Gender fetchGender(Character genderCode) {
        return fetchGenderByCode(genderCode);
    }

    private Profile fetchProfile(Long profileId, Long hospitalId) {
        return profileService.findActiveProfileByIdAndHospitalId(profileId, hospitalId);
    }
}
