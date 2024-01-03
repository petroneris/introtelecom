package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.mapper.PhoneMapper;
import com.snezana.introtelecom.mapper.UserMapper;
import com.snezana.introtelecom.repository.PackagePlanRepo;
import com.snezana.introtelecom.repository.RoleRepo;
import com.snezana.introtelecom.repository.UserRepo;
import com.snezana.introtelecom.repository.PhoneRepo;
import com.snezana.introtelecom.validation.PackageAddonPhoneServValidationService;
import com.snezana.introtelecom.validation.PhoneValidationService;
import com.snezana.introtelecom.validation.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPhoneServiceImpl implements UserPhoneService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PackagePlanRepo packagePlanRepo;
    private final PhoneRepo phoneRepo;
    private final PhoneValidationService phoneValidationService;
    private final UserValidationService userValidationService;
    private final PackageAddonPhoneServValidationService packageAddonPhoneServValidationService;
    private final PhoneMapper phoneMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveNewPhone(PhoneSaveDTO phoneSaveDto) {
        phoneValidationService.controlThePhoneNumberIsUnique(phoneSaveDto.getPhoneNumber());
        Phone phone = new Phone();
        phoneMapper.phoneSaveDtoToPhone(phoneSaveDto, phone, packagePlanRepo);
        phoneRepo.save(phone);
    }

    @Override
    public void changePackageCode (String phoneNumber, String packageCode){
        Phone phone = phoneValidationService.returnThePhoneIfExists(phoneNumber);
        PackagePlan packagePlan = packageAddonPhoneServValidationService.returnThePackagePlanIfPackageCodeExists(packageCode);
        phone.setPackagePlan(packagePlan);
        phoneRepo.save(phone);
    }

    @Override
    public void changePhoneStatus(String phoneNumber) {
        Phone phone = phoneValidationService.returnThePhoneIfExists(phoneNumber);
        if (phone.getPhoneStatus().equals(StatusType.PRESENT.getStatus())){
            phone.setPhoneStatus(StatusType.NOT_IN_USE.getStatus());
        } else {
            phone.setPhoneStatus(StatusType.PRESENT.getStatus());
        }
        String note = "Phone status changed at " + LocalDateTime.now();
        phone.setNote(note);
        phoneRepo.save(phone);
    }

    @Override
    public PhoneViewDTO getPhone (String phoneNumber) {
        Phone phone = phoneValidationService.returnThePhoneIfExists(phoneNumber);
        return phoneMapper.phoneToPhoneViewDTO(phone);
    }

    @Override
    public List<PhoneViewDTO> getAllAdminPhones() {
        List<Phone> phoneList = phoneRepo.findAllAdminPhones();
        return phoneMapper.phonesToPhonesViewDTO(phoneList);
    }

    @Override
    public List<PhoneViewDTO> getAllCustomersPhones() {
        List<Phone> phoneList = phoneRepo.findAllCustomerPhones();
        return phoneMapper.phonesToPhonesViewDTO(phoneList);
    }

    @Override
    public List<PhoneViewDTO> getPhonesByPackageCode(String packageCode) {
        packageAddonPhoneServValidationService.controlThePackageCodeExists(packageCode);
        List<Phone> phoneList = phoneRepo.findByPackageCode(packageCode);
        return phoneMapper.phonesToPhonesViewDTO(phoneList);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
@Override
public void saveNewUser(UserSaveDTO userSaveDto) {
    phoneValidationService.controlThePhoneExists(userSaveDto.getPhoneNumber());
    userValidationService.controlTheUserWithThisPhoneNumberAlreadyExists(userSaveDto.getPhoneNumber());
    userValidationService.controlTheUsernameIsUnique(userSaveDto.getUsername());
    User user = new User();
    userMapper.userSaveDtoToUser(userSaveDto, user, roleRepo, phoneRepo);
    user.setPassword(passwordEncoder.encode(userSaveDto.getPassword()));
    userRepo.save(user);
}

    @Override
    public List<UserViewDTO> getAllAdminsUsers() {
        List<User> userList = userRepo.findAllAdminsUsers();
        return userMapper.usersToUsersViewDTO(userList);
    }

    @Override
    public List<UserViewDTO> getAllCustomersUsers() {
        List<User> userList = userRepo.findAllCustomersUsers();
        return userMapper.usersToUsersViewDTO(userList);
    }


    @Override
    public UserViewDTO getUserByUsername(String username) {
        User user = userValidationService.returnTheUserWithThisUsernameIfExists(username);
        return userMapper.userToUserViewDTO(user);
    }

    @Override
    public UserViewDTO getUserByPhoneNumber(String phoneNumber) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        User user = userValidationService.returnTheUserWithThisPhoneNumberIfExists(phoneNumber);
        return userMapper.userToUserViewDTO(user);
    }

    @Override
    public void changeUserStatus(String phoneNumber) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        User user = userValidationService.returnTheUserWithThisPhoneNumberIfExists(phoneNumber);
        if (user.getUserStatus().equals(StatusType.PRESENT.getStatus())){
            user.setUserStatus(StatusType.NOT_IN_USE.getStatus());
        } else {
            user.setUserStatus(StatusType.PRESENT.getStatus());
        }
        userRepo.save(user);
    }

    @Override
    public void changeUserPassword(UserChangePasswordDTO userChangePasswordDto) {
        User user = userValidationService.returnTheUserWithThisUsernameIfExists(userChangePasswordDto.getUsername());
        String oldPassword = userChangePasswordDto.getOldPassword();
        userValidationService.checkIfOldPasswordIsValid(oldPassword, user.getPassword());
        user.setPassword(passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
    }

    @Override
    public void deleteUser(String username) {
        User user = userValidationService.returnTheUserWithThisUsernameIfExists(username);
        userValidationService.checkIfUserIsAdmin(user);
        userRepo.delete(user);
    }

}

