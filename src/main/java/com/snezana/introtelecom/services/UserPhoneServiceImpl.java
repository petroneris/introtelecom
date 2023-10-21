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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserPhoneServiceImpl implements UserPhoneService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserPhoneServiceImpl.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    PackagePlanRepo packagePlanRepo;

    @Autowired
    PhoneRepo phoneRepo;

    @Autowired
    PhoneValidationService phoneValidationService;

    @Autowired
    UserValidationService userValidationService;

    @Autowired
    PhoneMapper phoneMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void saveNewPhone(final PhoneSaveDTO phoneSaveDto) {
        phoneValidationService.controlThePhoneNumberIsUnique(phoneSaveDto.getPhoneNumber());
        final Phone phone = new Phone();
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
        String note = "Phone status changed in the date " + LocalDateTime.now();
        phone.setNote(note);
        phoneRepo.save(phone);
    }

    @Override
    public PhoneViewDTO getPhone (String phoneNumber) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
        PhoneViewDTO phoneViewDTO = phoneMapper.phoneToPhoneViewDTO(phone);
        return phoneViewDTO;
    }

    @Override
    public List<PhoneViewDTO> getAllAdminPhones() {
        List<Phone> phoneList = phoneRepo.findAllAdminPhones();
        List<PhoneViewDTO> phoneViewDTOList = phoneMapper.phoneToPhoneViewDTO(phoneList);
        return phoneViewDTOList;
    }

    @Override
    public List<PhoneViewDTO> getAllCustomersPhones() {
        List<Phone> phoneList = phoneRepo.findAllCustomerPhones();
        List<PhoneViewDTO> phoneViewDTOList = phoneMapper.phoneToPhoneViewDTO(phoneList);
        return phoneViewDTOList;
    }

    @Override
    public List<PhoneViewDTO> getPhonesByPackageCode(String packageCode) {
        phoneValidationService.controlThePackageCodeExists(packageCode);
        List<Phone> phoneList = phoneRepo.findByPackageCode(packageCode);
        List<PhoneViewDTO> phoneViewDTOList = phoneMapper.phoneToPhoneViewDTO(phoneList);
        return phoneViewDTOList;
    }

    @Override
    public void saveNewUser(final UserSaveDTO userSaveDto) {
        phoneValidationService.controlThePhoneExists(userSaveDto.getPhone());
        userValidationService.controlTheUserWithThisPhoneNumberExists(userSaveDto.getPhone());
        userValidationService.controlTheUsernameIsUnique(userSaveDto.getUsername());
        final User user = new User();
        userMapper.userSaveDtoToUser(userSaveDto, user, roleRepo, phoneRepo);
        user.setPassword(passwordEncoder.encode(userSaveDto.getPassword()));
        userRepo.save(user);
    }

    @Override
    public List<UserViewDTO> getAllAdminsUsers() {
        List<User> userList = userRepo.findAllAdminsUsers();
        List<UserViewDTO> userViewDTOList = userMapper.userToUserViewDTO(userList);
        return userViewDTOList;
    }

    @Override
    public List<UserViewDTO> getAllCustomersUsers() {
        List<User> userList = userRepo.findAllCustomersUsers();
        List<UserViewDTO> userViewDTOList = userMapper.userToUserViewDTO(userList);
        return userViewDTOList;
    }


    @Override
    public UserViewDTO getUserByUsername(String username) {
        userValidationService.controlTheUsernameExists(username);
        User user = userRepo.findByUsername(username);
        UserViewDTO userViewDTO = userMapper.userToUserViewDTO(user);
        return userViewDTO;
    }

    @Override
    public UserViewDTO getUserByPhoneNumber(String phoneNumber) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        User user = userRepo.findByPhoneNumber(phoneNumber);
        UserViewDTO userViewDTO = userMapper.userToUserViewDTO(user);
        return userViewDTO;
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

