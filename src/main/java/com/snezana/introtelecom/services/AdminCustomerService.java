package com.snezana.introtelecom.services;

import com.snezana.introtelecom.dto.*;

import java.util.List;

public interface AdminCustomerService {

    public void saveNewAdmin (AdminSaveDTO adminSaveDto);

    public void updateAdmin (AdminSaveDTO adminSaveDto, Long id);

    public AdminViewDTO getAdminById (Long adminId);

    public AdminViewDTO getAdminByPhone (String phoneNumber);

    public AdminViewDTO getAdminByPersonalNumber (String personalNumber);

    public AdminViewDTO getAdminByEmail (String email);

    public List<AdminViewDTO> getAllAdmins();

    public void saveNewCustomer (CustomerSaveDTO customerSaveDto);

    public void updateCustomer (CustomerSaveDTO customerSaveDto, Long id);

    public CustomerViewDTO getCustomerById (Long customerId);

    public CustomerViewDTO getCustomerByPhone (String phoneNumber);

    public CustomerViewDTO getCustomerByPersonalNumber (String personalNumber);

    public CustomerViewDTO getCustomerByEmail (String email);

    public List<CustomerViewDTO> getAllCustomers();

    public CustomerViewDTO getCustomerByUsername (String username);

    public void addPhoneToCustomer (Long customerId, String phoneNumber);

    public void removePhoneFromCustomer (Long customerId, String phoneNumber);
}
