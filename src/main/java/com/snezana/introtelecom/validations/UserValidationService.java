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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public void controlTheUserWithThisPhoneNumberAlreadyExists(String phoneNumber) {
        Optional<User> userOptional = userRepo.findByPhoneNumberOpt(phoneNumber);
        userOptional.ifPresent(user -> {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The user with this phoneNumber = " + user.getPhoneNumber() + " already exists in database!");
        });
    }
//???
    public User returnTheUserWithThisPhoneNumberIfExists(String phoneNumber) {
        return userRepo.findByPhoneNumberOpt(phoneNumber)
                .orElseThrow(() ->  new IllegalItemFieldException(RestAPIErrorMessage.ITEM_NOT_FOUND, "There is no user with this phoneNumber!"));
    }

    public void controlTheUsernameIsUnique(String username) {
        Optional<User> userOptional = userRepo.findByUsernameOpt(username);
        userOptional.ifPresent(user -> {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, "The user with the username = " +username + " already exists in database!");
        });
    }

    public User returnTheUserWithThisUsernameIfExists(String username) {
        return userRepo.findByUsernameOpt(username)
                .orElseThrow(() ->  new IllegalItemFieldException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The user = " +username + " doesn't exist in database!"));
    }

    public void checkIfOldPasswordIsValid(String oldPassword, String newPassword) {
        boolean match = passwordEncoder.matches(oldPassword, newPassword);
        if (!match) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.ITEMS_NOT_MATCH, "Input password doesn't match old password!");
        }
    }

    public void checkIfUserIsAdmin(User user) {
        if(user.getPhone().getPackagePlan().getPackageCode().equals(PackagePlanType.ADM.getPackageCode())) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "Can't delete this user!");
        }
    }

    public void checkIfUserIsCustomer (User user){
        if(user.getPhone().getPackagePlan().getPackageCode().equals((PackagePlanType.ADM.getPackageCode()))){
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "There is no customer with that username!");
        }
    }

}
