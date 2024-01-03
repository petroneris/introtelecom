package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.*;

import java.util.List;

public interface AdminCustomerService {

    void saveNewAdmin (AdminSaveDTO adminSaveDto);

    void updateAdmin (AdminSaveDTO adminSaveDto, Long id);

    AdminViewDTO getAdminById (Long adminId);

    AdminViewDTO getAdminByPhone (String phoneNumber);

    AdminViewDTO getAdminByPersonalNumber (String personalNumber);

    AdminViewDTO getAdminByEmail (String email);

    List<AdminViewDTO> getAllAdmins();

    void saveNewCustomer (CustomerSaveDTO customerSaveDto);

    void updateCustomer (CustomerSaveDTO customerSaveDto, Long id);

    CustomerViewDTO getCustomerById (Long customerId);

    CustomerViewDTO getCustomerByPhone (String phoneNumber);

    CustomerViewDTO getCustomerByPersonalNumber (String personalNumber);

    CustomerViewDTO getCustomerByEmail (String email);

    List<CustomerViewDTO> getAllCustomers();

    CustomerViewDTO getCustomerByUsername (String username);

    void addPhoneToCustomer (Long customerId, String phoneNumber);

    void removePhoneFromCustomer (Long customerId, String phoneNumber);
}
