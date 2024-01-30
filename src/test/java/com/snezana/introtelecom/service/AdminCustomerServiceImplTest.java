package com.snezana.introtelecom.service;

import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.mapper.AdminMapper;
import com.snezana.introtelecom.mapper.CustomerMapper;
import com.snezana.introtelecom.repository.AdminRepo;
import com.snezana.introtelecom.repository.CustomerRepo;
import com.snezana.introtelecom.repository.PhoneRepo;
import com.snezana.introtelecom.validation.AdminValidationService;
import com.snezana.introtelecom.validation.CustomerValidationService;
import com.snezana.introtelecom.validation.PhoneValidationService;
import com.snezana.introtelecom.validation.UserValidationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminCustomerServiceImplTest {

    AdminCustomerServiceImpl adminCustomerService;

    @Mock PhoneRepo phoneRepo;
    @Mock AdminRepo adminRepo;
    @Mock CustomerRepo customerRepo;
    @Mock PhoneValidationService phoneValidationService;
    @Mock UserValidationService userValidationService;
    @Mock AdminValidationService adminValidationService;
    @Mock CustomerValidationService customerValidationService;
    @Mock AdminMapper adminMapper;
    @Mock CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        adminCustomerService = new AdminCustomerServiceImpl(phoneRepo, adminRepo, customerRepo, phoneValidationService, userValidationService, adminValidationService, customerValidationService, adminMapper, customerMapper);
        // Reset mocks. We'll put fresh settings in for each test.
        reset(phoneRepo);
        reset(adminRepo);
        reset(customerRepo);
        reset(phoneValidationService);
        reset(userValidationService);
        reset(adminValidationService);
        reset(customerValidationService);
        reset(adminMapper);
        reset(customerMapper);
    }

    @Test
    void saveNewAdmin() {
        // given
        when(phoneValidationService.returnThePhoneIfExists(anyString())).thenReturn(new Phone());
        // when

        // then

    }

    @Test
    void updateAdmin() {
    }

    @Test
    void getAdminById() {
    }

    @Test
    void getAdminByPhone() {
    }

    @Test
    void getAdminByPersonalNumber() {
    }

    @Test
    void getAdminByEmail() {
    }

    @Test
    void getAllAdmins() {
    }

    @Test
    void saveNewCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void getCustomerById() {
    }

    @Test
    void getCustomerByPhone() {
    }

    @Test
    void getCustomerByPersonalNumber() {
    }

    @Test
    void getCustomerByEmail() {
    }

    @Test
    void getAllCustomers() {
    }

    @Test
    void getCustomerByUsername() {
    }

    @Test
    void addPhoneToCustomer() {
    }

    @Test
    void removePhoneFromCustomer() {
    }
}