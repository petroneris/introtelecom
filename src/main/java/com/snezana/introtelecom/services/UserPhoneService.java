package com.snezana.introtelecom.services;

import com.snezana.introtelecom.dto.*;


import java.util.List;

public interface UserPhoneService {

    public void saveNewPhone (final PhoneSaveDTO phoneSaveDto);

    public void changePackageCode (String phoneNumber, String packageCode);

    public void changePhoneStatus (String phoneNumber);

    public PhoneViewDTO getPhone (String phoneNumber);

    public List<PhoneViewDTO> getAllAdminPhones();

    public List<PhoneViewDTO> getAllCustomersPhones();

    public List<PhoneViewDTO> getPhonesByPackageCode(String packageCode);

    public void saveNewUser(final UserSaveDTO userSaveDto);

    public List<UserViewDTO> getAllAdminsUsers();

    public List<UserViewDTO> getAllCustomersUsers();

    UserViewDTO getUserByUsername(String username);

    UserViewDTO getUserByPhoneNumber(String phoneNumber);

    public void changeUserStatus(String phoneNumber);

    public void changeUserPassword(UserChangePasswordDTO userChangePasswordDto);

    public void deleteUser (String username);

}
