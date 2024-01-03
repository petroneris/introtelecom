package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.*;

import java.util.List;

public interface UserPhoneService {

    void saveNewPhone (PhoneSaveDTO phoneSaveDto);

    void changePackageCode (String phoneNumber, String packageCode);

    void changePhoneStatus (String phoneNumber);

    PhoneViewDTO getPhone (String phoneNumber);

    List<PhoneViewDTO> getAllAdminPhones();

    List<PhoneViewDTO> getAllCustomersPhones();

    List<PhoneViewDTO> getPhonesByPackageCode(String packageCode);

    void saveNewUser(UserSaveDTO userSaveDto);

    List<UserViewDTO> getAllAdminsUsers();

    List<UserViewDTO> getAllCustomersUsers();

    UserViewDTO getUserByUsername(String username);

    UserViewDTO getUserByPhoneNumber(String phoneNumber);

    void changeUserStatus(String phoneNumber);

    void changeUserPassword(UserChangePasswordDTO userChangePasswordDto);

    void deleteUser (String username);

}
