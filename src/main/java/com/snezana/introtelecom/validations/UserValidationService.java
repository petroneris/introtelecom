package com.snezana.introtelecom.validations;

import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.exceptions.IllegalItemFieldException;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.PackagePlanRepo;
import com.snezana.introtelecom.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PackagePlanRepo packagePlanRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void controlTheUserWithThisPhoneNumberExists(String phoneNumber) {
        Optional<User> userOptional = userRepo.findByPhoneNumberOpt(phoneNumber);
        if(userOptional.isPresent()){
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The user with the phoneNumber = " +phoneNumber + " exists in database!");
        }
    }

    public void controlTheUsernameIsUnique(String username) {
        Optional<User> userOptional = userRepo.findByUsernameOpt(username);
        if(userOptional.isPresent()){
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The user with the username = " +username + " exists in database!");
        }
    }

    public void controlTheUsernameExists (String username){
        userRepo.findByUsernameOpt(username)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The user = " +username + " doesn't exist in database!" ));
    }

    public void checkIfValidOldPassword(String oldPassword, String newPassword) {
        boolean match = passwordEncoder.matches(oldPassword, newPassword);
        if (!match) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEMS_NOT_MATCH, "Input password doesn't match old password!");
        }
    }

    public void checkIfUserIsAdmin(String packageCode) {
        if(packageCode.equals(PackagePlanType.ADM.getPackageCode())) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "Can't delete this user!");
        }
    }

    public void checkIfValidUserSaveDTO(BindingResult result) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            System.out.println("result.hasErrors() - in checkIfValidUserSaveDTO");
            throw new MethodArgumentNotValidException(null, result);
        }
    }

    public void checkIfUserIsCustomer (String username){
        User user = userRepo.findByUsername(username);
        Phone phone = user.getPhone();
        if(phone.getPackagePlan().getPackageCode().equals((PackagePlanType.ADM.getPackageCode()))){
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "There is no customer with that username!");
        }
    }

}
