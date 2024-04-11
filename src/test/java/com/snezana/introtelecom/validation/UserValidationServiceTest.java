package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.exception.IllegalItemFieldException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserValidationService userValidationService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testControlTheUserWithThisPhoneNumberAlreadyExists_alreadyExists() {
        String phoneNumber = "0732283498";
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        String description = "The user with this phone number = " + phoneNumber + " already exists in database!";
        when(userRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.of(user));

        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            userValidationService.controlTheUserWithThisPhoneNumberAlreadyExists(phoneNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());

    }

    @Test
    void testControlTheUserWithThisPhoneNumberAlreadyExists_doesNotExist() {
        String phoneNumber = "0732283498";
        when(userRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            userValidationService.controlTheUserWithThisPhoneNumberAlreadyExists(phoneNumber);
        });

    }

    @Test
    void testReturnTheUserWithThisPhoneNumberIfExists_exists() {
        String phoneNumber = "0732283498";
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        when(userRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.of(user));
        User newUser = userValidationService.returnTheUserWithThisPhoneNumberIfExists(phoneNumber);
        assertThat(newUser.getPhoneNumber()).isEqualTo(phoneNumber);

    }

    @Test
    void testReturnTheUserWithThisPhoneNumberIfExists_doesNotExist() {
        String phoneNumber = "0732283498";
        String description = "There is no user with this phone number!";
        when(userRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.empty());
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            userValidationService.returnTheUserWithThisPhoneNumberIfExists(phoneNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheUsernameIsUnique_isUnique() {
        String username = "sneza3";
        when(userRepo.findByUsernameOpt(username)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            userValidationService.controlTheUsernameIsUnique(username);
        });
    }

    @Test
    void testControlTheUsernameIsUnique_isNotUnique() {
        String username = "sneza3";
        String description = "The user with the username = " +username + " already exists in database!";
        User user = new User();
        user.setUsername(username);
        when(userRepo.findByUsernameOpt(username)).thenReturn(Optional.of(user));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            userValidationService.controlTheUsernameIsUnique(username);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());

    }

    @Test
    void testReturnTheUserWithThisUsernameIfExists_exists() {
        String username = "sneza3";
        User user = new User();
        user.setUsername(username);
        when(userRepo.findByUsernameOpt(username)).thenReturn(Optional.of(user));
        User newUser = userValidationService.returnTheUserWithThisUsernameIfExists(username);
        assertThat(newUser.getUsername()).isEqualTo(username);
    }

    @Test
    void testReturnTheUserWithThisUsernameIfExists_doesNotExists() {
        String username = "sneza3";
        String description = "The user = " +username + " doesn't exist in database!";
        when(userRepo.findByUsernameOpt(username)).thenReturn(Optional.empty());
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            userValidationService.returnTheUserWithThisUsernameIfExists(username);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testCheckIfOldPasswordIsValid_valid() {
        String oldPassword = "password1";
        String newPassword = "password1";
        when(passwordEncoder.matches(oldPassword, newPassword)).thenReturn(true);
        assertDoesNotThrow(() -> {
            userValidationService.checkIfOldPasswordIsValid(oldPassword, newPassword);
        });
    }

    @Test
    void testCheckIfOldPasswordIsValid_notValid() {
        String oldPassword = "password1";
        String newPassword = "password2";
        String description = "Invalid old password!";
        when(passwordEncoder.matches(oldPassword, newPassword)).thenReturn(false);
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            userValidationService.checkIfOldPasswordIsValid(oldPassword, newPassword);
        });
        assertEquals(RestAPIErrorMessage.ITEMS_NOT_MATCH, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testCheckIfUserIsAdmin_isAdmin() {
        String description = "Can't delete this user!";
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(PackagePlanType.ADM.getPackageCode());
        Phone phone = new Phone();
        phone.setPackagePlan(packagePlan);
        User user = new User();
        user.setPhone(phone);
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            userValidationService.checkIfUserIsAdmin(user);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testCheckIfUserIsAdmin_isNotAdmin() {
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(PackagePlanType.PST13.getPackageCode());
        Phone phone = new Phone();
        phone.setPackagePlan(packagePlan);
        User user = new User();
        user.setPhone(phone);
        assertDoesNotThrow(() -> {
            userValidationService.checkIfUserIsAdmin(user);
        });
    }

    @Test
    void testCheckIfUserIsCustomer_isCustomer() {
        String description = "There is no customer with that username!";
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(PackagePlanType.ADM.getPackageCode());
        Phone phone = new Phone();
        phone.setPackagePlan(packagePlan);
        User user = new User();
        user.setPhone(phone);
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            userValidationService.checkIfUserIsCustomer(user);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testCheckIfUserIsCustomer_isNotCustomer() {
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(PackagePlanType.PST13.getPackageCode());
        Phone phone = new Phone();
        phone.setPackagePlan(packagePlan);
        User user = new User();
        user.setPhone(phone);
        assertDoesNotThrow(() -> {
            userValidationService.checkIfUserIsCustomer(user);
        });
    }
}