package com.snezana.introtelecom.validations;

import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.exceptions.IllegalItemFieldException;
import com.snezana.introtelecom.repositories.PhoneRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhoneValidationService {

    private final PhoneRepo phoneRepo;

    public void controlThePhoneNumberIsUnique(String phoneNumber) {
        Optional<Phone> phoneOptional = phoneRepo.findByPhoneNumberOpt(phoneNumber);
        phoneOptional.ifPresent(phone -> {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The phone number = " + phone.getPhoneNumber() + " already exists in database!");
        });
    }

    public void controlThePhoneExists (String phoneNumber){
        phoneRepo.findByPhoneNumberOpt(phoneNumber)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The phone number = " +phoneNumber + " doesn't exist in database!" ));
    }

    public Phone returnThePhoneIfExists(String phoneNumber){
        return phoneRepo.findByPhoneNumberOpt(phoneNumber)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The phone number = " +phoneNumber + " doesn't exist in database!" ));
    }

    public void controlThisPhoneHasTheAdminPackageCode(Phone phone) {
        if (!phone.getPackagePlan().getPackageCode().equals(PackagePlanType.ADM.getPackageCode())){
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEMS_NOT_MATCH, "This phone number has no admin package code!");
        }
    }

    public void controlThisPhoneHasCustomersPackageCode(Phone phone){
        if(phone.getPackagePlan().getPackageCode().equals((PackagePlanType.ADM.getPackageCode()))){
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "This phone number has no customer's package code!");
        }
    }
}
