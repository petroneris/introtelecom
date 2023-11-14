package com.snezana.introtelecom.services;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.mapper.PhoneMapper;
import com.snezana.introtelecom.mapper.UserMapper;
import com.snezana.introtelecom.repositories.PackagePlanRepo;
import com.snezana.introtelecom.repositories.RoleRepo;
import com.snezana.introtelecom.repositories.UserRepo;
import com.snezana.introtelecom.repositories.PhoneRepo;
import com.snezana.introtelecom.validations.PhoneValidationService;
import com.snezana.introtelecom.validations.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPhoneServiceImpl implements UserPhoneService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserPhoneServiceImpl.class);

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PackagePlanRepo packagePlanRepo;
    private final PhoneRepo phoneRepo;
    private final PhoneValidationService phoneValidationService;
    private final UserValidationService userValidationService;
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
        phoneValidationService.controlThePhoneExists(phoneNumber);
        phoneValidationService.controlThePackageCodeExists(packageCode);
        Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
        PackagePlan packagePlan = packagePlanRepo.findByPackageCode(packageCode);
        phone.setPackagePlan(packagePlan);
        phoneRepo.save(phone);
    }

    @Override
    public void changePhoneStatus(String phoneNumber) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
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
        phoneValidationService.controlThePhoneExists(phoneNumber);
        Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
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
        phoneValidationService.controlThePackageCodeExists(packageCode);
        List<Phone> phoneList = phoneRepo.findByPackageCode(packageCode);
        return phoneMapper.phonesToPhonesViewDTO(phoneList);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
@Override
public void saveNewUser(UserSaveDTO userSaveDto) {
    phoneValidationService.controlThePhoneExists(userSaveDto.getPhoneNumber());
    userValidationService.controlTheUserWithThisPhoneNumberExists(userSaveDto.getPhoneNumber());
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
        userValidationService.controlTheUsernameExists(username);
        User user = userRepo.findByUsername(username);
        return userMapper.userToUserViewDTO(user);
    }

    @Override
    public UserViewDTO getUserByPhoneNumber(String phoneNumber) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        User user = userRepo.findByPhoneNumber(phoneNumber);
        return userMapper.userToUserViewDTO(user);
    }

    @Override
    public void changeUserStatus(String phoneNumber) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        User user = userRepo.findByPhoneNumber(phoneNumber);
        if (user.getUserStatus().equals(StatusType.PRESENT.getStatus())){
            user.setUserStatus(StatusType.NOT_IN_USE.getStatus());
        } else {
            user.setUserStatus(StatusType.PRESENT.getStatus());
        }
        userRepo.save(user);
    }

    @Override
    public void changeUserPassword(UserChangePasswordDTO userChangePasswordDto) {
        userValidationService.controlTheUsernameExists(userChangePasswordDto.getUsername());
        User user = userRepo.findByUsername(userChangePasswordDto.getUsername());
        String oldPassword = userChangePasswordDto.getOldPassword();
        userValidationService.checkIfValidOldPassword(oldPassword, user.getPassword());
        user.setPassword(passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
    }

    @Override
    public void deleteUser(String username) {
        userValidationService.controlTheUsernameExists(username);
        User user = userRepo.findByUsername(username);
        String packageCode = user.getPhone().getPackagePlan().getPackageCode();
        userValidationService.checkIfUserIsAdmin(packageCode);
        userRepo.delete(user);
    }

}

