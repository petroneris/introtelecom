package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.exception.IllegalItemFieldException;
import com.snezana.introtelecom.exception.ItemNotFoundException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.AdminRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminValidationServiceTest {

    @Mock
    private AdminRepo adminRepo;

    @InjectMocks
    private AdminValidationService adminValidationService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testControlThePersonalNumberIsUnique_isUnique() {
        String personalNumber = "3998734832";
        when(adminRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            adminValidationService.controlThePersonalNumberIsUnique(personalNumber);
        });
    }

    @Test
    void testControlThePersonalNumberIsUnique_isNotUnique() {
        String personalNumber = "3998734832";
        Admin admin = new Admin();
        admin.setPersonalNumber(personalNumber);
        String description = personalNumber + " personal number already exists in database!";
        when(adminRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.of(admin));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            adminValidationService.controlThePersonalNumberIsUnique(personalNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnAdminWithThatPersonalNumberIfExists_exists() {
        String personalNumber = "3998734832";
        Admin admin = new Admin();
        admin.setPersonalNumber(personalNumber);
        when(adminRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.of(admin));
        Admin newAdmin = adminValidationService.returnAdminWithThatPersonalNumberIfExists(personalNumber);
        assertThat(newAdmin.getPersonalNumber()).isEqualTo(personalNumber);
    }

    @Test
    void testReturnAdminWithThatPersonalNumberIfExists_doesNotExist() {
        String personalNumber = "3998734832";
        String description = "The admin with that personal number doesn't exist in database!";
        when(adminRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            adminValidationService.returnAdminWithThatPersonalNumberIfExists(personalNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheOtherAdminHasThisPhone_itHas() {
        String phoneNumber = "0732283498";
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        Admin admin = new Admin();
        admin.setPhone(phone);
        String description = "The admin with " + phoneNumber + " phone number already exists in database!";
        when(adminRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.of(admin));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            adminValidationService.controlTheOtherAdminHasThisPhone(phoneNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheOtherAdminHasThisPhone_itHasNot() {
        String phoneNumber = "0732283498";
        when(adminRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            adminValidationService.controlTheOtherAdminHasThisPhone(phoneNumber);
        });

    }

    @Test
    void testReturnTheAdminWithThatIdIfExists_exists() {
        Long id = 3L;
        Admin admin =  new Admin();
        admin.setAdminId(id);
        when(adminRepo.findByAdminIdOpt(id)).thenReturn(Optional.of(admin));
        Admin newAdmin = adminValidationService.returnTheAdminWithThatIdIfExists(id);
        assertThat(newAdmin.getAdminId()).isEqualTo(id);
    }

    @Test
    void testReturnTheAdminWithThatIdIfExists_doesNotExist() {
        Long id = 3L;
        String description = "The admin with that id doesn't exist in database!";
        when(adminRepo.findByAdminIdOpt(id)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            adminValidationService.returnTheAdminWithThatIdIfExists(id);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnTheAdminWithThatPhoneIfExists_exists() {
        String phoneNumber = "0732283498";
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        Admin admin = new Admin();
        admin.setPhone(phone);
        when(adminRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.of(admin));
        Admin newAdmin = adminValidationService.returnTheAdminWithThatPhoneIfExists(phoneNumber);
        assertThat(newAdmin.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testReturnTheAdminWithThatPhoneIfExists_doesNotExist() {
        String phoneNumber = "0732283498";
        String description = "The admin with that phone number doesn't exist in database!";
        when(adminRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            adminValidationService.returnTheAdminWithThatPhoneIfExists(phoneNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnTheAdminWithThatEmailIfExists_exists() {
        String email = "sneza@bluephone.com";
        Admin admin = new Admin();
        admin.setEmail(email);
        when(adminRepo.findByEmailOpt(email)).thenReturn(Optional.of(admin));
        Admin newAdmin = adminValidationService.returnTheAdminWithThatEmailIfExists(email);
        assertThat(newAdmin.getEmail()).isEqualTo(email);
    }

    @Test
    void testReturnTheAdminWithThatEmailIfExists_doesNotExist() {
        String email = "sneza@bluephone.com";
        String description = "The admin with that email doesn't exist in database!";
        when(adminRepo.findByEmailOpt(email)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            adminValidationService.returnTheAdminWithThatEmailIfExists(email);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheOtherAdminHasThisPersonalNumber_itHasNot() {
        String personalNumber = "3998734832";
        Long id = 3L;
        when(adminRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            adminValidationService.controlTheOtherAdminHasThisPersonalNumber(personalNumber, id);
        });
    }

    @Test
    void testControlTheOtherAdminHasThisPersonalNumber_itHasNot2() {
        String personalNumber = "3998734832";
        Long id = 3L;
        Admin admin = new Admin();
        admin.setPersonalNumber(personalNumber);
        admin.setAdminId(id);
        when(adminRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.of(admin));
        assertDoesNotThrow(() -> {
            adminValidationService.controlTheOtherAdminHasThisPersonalNumber(personalNumber, id);
        });

    }

    @Test
    void testControlTheOtherAdminHasThisPersonalNumber_itHas() {
        String personalNumber = "3998734832";
        Long adminId = 3L;
        Long otherId = 4L;
        Admin admin = new Admin();
        admin.setPersonalNumber(personalNumber);
        admin.setAdminId(adminId);
        String description = "Another admin with that personal number already exists in database!";
        when(adminRepo.findByPersonalNumberOpt(personalNumber)).thenReturn(Optional.of(admin));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            adminValidationService.controlTheOtherAdminHasThisPersonalNumber(personalNumber, otherId);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());

    }

    @Test
    void testControlTheOtherAdminHasThisEmail_itHasNot() {
        String email = "sneza@bluephone.com";
        Long id = 3L;
        when(adminRepo.findByEmailOpt(email)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            adminValidationService.controlTheOtherAdminHasThisEmail(email, id);
        });
    }

    @Test
    void testControlTheOtherAdminHasThisEmail_itHasNot2() {
        String email = "sneza@bluephone.com";
        Long id = 3L;
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setAdminId(id);
        when(adminRepo.findByEmailOpt(email)).thenReturn(Optional.of(admin));
        assertDoesNotThrow(() -> {
            adminValidationService.controlTheOtherAdminHasThisEmail(email, id);
        });
    }

    @Test
    void testControlTheOtherAdminHasThisEmail_itHas() {
        String email = "sneza@bluephone.com";
        Long id = 3L;
        Long otherId = 4L;
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setAdminId(id);
        String description = "Another admin with that email already exists in database!";
        when(adminRepo.findByEmailOpt(email)).thenReturn(Optional.of(admin));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            adminValidationService.controlTheOtherAdminHasThisEmail(email, otherId);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlThatAnotherAdminHasThisPhoneNumber_itHasNot() {
        String phoneNumber = "0732283498";
        Long id = 3L;
        when(adminRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            adminValidationService.controlThatAnotherAdminHasThisPhoneNumber(phoneNumber, id);
        });
    }

    @Test
    void testControlThatAnotherAdminHasThisPhoneNumber_itHasNot2() {
        String phoneNumber = "0732283498";
        Long id = 3L;
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        Admin admin = new Admin();
        admin.setPhone(phone);
        admin.setAdminId(id);
        when(adminRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.of(admin));
        assertDoesNotThrow(() -> {
            adminValidationService.controlThatAnotherAdminHasThisPhoneNumber(phoneNumber, id);
        });
    }

    @Test
    void testControlThatAnotherAdminHasThisPhoneNumber_itHas() {
        String phoneNumber = "0732283498";
        Long id = 3L;
        Long otherId = 4L;
        String description = "Another admin with this phone number already exists in database!";
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        Admin admin = new Admin();
        admin.setPhone(phone);
        admin.setAdminId(id);
        when(adminRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.of(admin));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            adminValidationService.controlThatAnotherAdminHasThisPhoneNumber(phoneNumber, otherId);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }


}