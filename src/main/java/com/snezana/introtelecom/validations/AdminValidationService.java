package com.snezana.introtelecom.validations;

import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.exceptions.IllegalItemFieldException;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.AdminRepo;
import com.snezana.introtelecom.repositories.PhoneRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminValidationService {

    private final PhoneRepo phoneRepo;
    private final AdminRepo adminRepo;

    public void controlThePersonalNumberIsUnique(String personalNumber) {
        Optional<Admin> adminOptional = adminRepo.findByPersonalNumberOpt(personalNumber);
        if (adminOptional.isPresent()) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "This personal number exists in database!");
        }
    }

    public void controlThePersonalNumberExists(String personalNumber) {
       adminRepo.findByPersonalNumberOpt(personalNumber)
               .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The admin with that personalNumber doesn't exist in database!"));

    }

    public void controlTheOtherAdminHasThisPhone(String phoneNumber) {
        Optional<Admin> adminOptional = adminRepo.findByPhoneNumberOpt(phoneNumber);
        if (adminOptional.isPresent()) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The admin with that phone number exists in database!");
        }
    }

    public void controlTheAdminWithThatIdExists (Long adminId){
        adminRepo.findByAdminIdOpt(adminId)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The admin with that Id doesn't exist in database!"));
    }

    public void controlTheEmailExists (String email){
        adminRepo.findByEmailOpt(email)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The admin with that email doesn't exist in database!"));
    }

    public void controlUpdateTheOtherAdminHasThisPersonalNumber(String personalNumber, Long adminId){
        Optional<Admin> adminOptional = adminRepo.findByPersonalNumberOpt(personalNumber);
        if (adminOptional.isPresent()) {
            Admin admin = adminRepo.findByPersonalNumber(personalNumber);
            if(!admin.getAdminId().equals(adminId)){
                throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The other admin with that personal number exists in database!");
            }
        }
    }

    public void controlUpdateTheOtherAdminHasThisEmail(String email, Long adminId){
        Optional<Admin> adminOptional = adminRepo.findByEmailOpt(email);
        if (adminOptional.isPresent()) {
            Admin admin = adminRepo.findAdminByEmail(email);
            if(!admin.getAdminId().equals(adminId)){
                throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The other admin with that email exists in database!");
            }
        }
    }

    public void controlUpdateTheOtherAdminHasThisPhone(String phoneNumber, Long adminId){
        Optional<Admin> adminOptional = adminRepo.findByPhoneNumberOpt(phoneNumber);
        if (adminOptional.isPresent()) {
            Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
            Admin admin = adminRepo.findByPhone(phone);
            if(!admin.getAdminId().equals(adminId)){
                throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The other admin with that phone exists in database!");
            }
        }
    }


}

