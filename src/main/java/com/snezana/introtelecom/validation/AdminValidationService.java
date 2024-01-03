package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.exception.IllegalItemFieldException;
import com.snezana.introtelecom.exception.ItemNotFoundException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.AdminRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminValidationService {

    private final AdminRepo adminRepo;

    public void controlThePersonalNumberIsUnique(String personalNumber) {
        Optional<Admin> adminOptional = adminRepo.findByPersonalNumberOpt(personalNumber);
        adminOptional.ifPresent(admin -> {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, admin.getPersonalNumber() + " personal number already exists in database!");
        });
    }

    public Admin returnAdminWithThatPersonalNumberIfExists(String personalNumber) {
        return adminRepo.findByPersonalNumberOpt(personalNumber)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The admin with that personalNumber doesn't exist in database!"));
    }

    public void controlTheOtherAdminHasThisPhone(String phoneNumber) {
        Optional<Admin> adminOptional = adminRepo.findByPhoneNumberOpt(phoneNumber);
        adminOptional.ifPresent(admin -> {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The admin with " + admin.getPhone().getPhoneNumber() + " phone number already exists in database!");
        });
    }

    public Admin returnTheAdminWithThatIdIfExists (Long adminId){
        return adminRepo.findByAdminIdOpt(adminId)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The admin with that id doesn't exist in database!"));
    }

    public Admin returnTheAdminWithThatPhoneIfExists (String phoneNumber){
        return adminRepo.findByPhoneNumberOpt(phoneNumber)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The admin with that phone doesn't exist in database!"));
    }

    public Admin returnTheAdminWithThatEmailIfExists (String email){
        return adminRepo.findByEmailOpt(email)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The admin with that email doesn't exist in database!"));
    }

    public void controlTheOtherAdminHasThisPersonalNumber(String personalNumber, Long adminId){
        Optional<Admin> adminOptional = adminRepo.findByPersonalNumberOpt(personalNumber);
        adminOptional.ifPresent(admin -> {
            if(!admin.getAdminId().equals(adminId)){
                throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The other admin with that personal number already exists in database!");
            }
        });
    }

    public void controlTheOtherAdminHasThisEmail(String email, Long adminId){
        Optional<Admin> adminOptional = adminRepo.findByEmailOpt(email);
        adminOptional.ifPresent(admin -> {
            if(!admin.getAdminId().equals(adminId)){
                throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The other admin with that email already exists in database!");
            }
        });
    }

    public void controlTheOtherAdminHasThisPhone(String phoneNumber, Long adminId){
        Optional<Admin> adminOptional = adminRepo.findByPhoneNumberOpt(phoneNumber);
        adminOptional.ifPresent(admin -> {
            if(!admin.getAdminId().equals(adminId)){
                throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The other admin with that phone already exists in database!");
            }
        });
    }

}

