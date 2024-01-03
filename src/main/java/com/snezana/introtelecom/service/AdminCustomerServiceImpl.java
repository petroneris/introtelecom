package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.AdminSaveDTO;
import com.snezana.introtelecom.dto.AdminViewDTO;
import com.snezana.introtelecom.dto.CustomerSaveDTO;
import com.snezana.introtelecom.dto.CustomerViewDTO;
import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.entity.Customer;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.mapper.AdminMapper;
import com.snezana.introtelecom.mapper.CustomerMapper;
import com.snezana.introtelecom.repository.AdminRepo;
import com.snezana.introtelecom.repository.CustomerRepo;
import com.snezana.introtelecom.repository.PhoneRepo;
import com.snezana.introtelecom.validation.AdminValidationService;
import com.snezana.introtelecom.validation.CustomerValidationService;
import com.snezana.introtelecom.validation.PhoneValidationService;
import com.snezana.introtelecom.validation.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminCustomerServiceImpl implements AdminCustomerService{

    private final PhoneRepo phoneRepo;
    private final AdminRepo adminRepo;
    private final CustomerRepo customerRepo;
    private final PhoneValidationService phoneValidationService;
    private final UserValidationService userValidationService;
    private final AdminValidationService adminValidationService;
    private final CustomerValidationService customerValidationService;
    private final AdminMapper adminMapper;
    private final CustomerMapper customerMapper;

    @Override
    public void saveNewAdmin(AdminSaveDTO adminSaveDto) {
        Phone phone = phoneValidationService.returnThePhoneIfExists(adminSaveDto.getPhoneNumber());
        phoneValidationService.controlThisPhoneHasTheAdminPackageCode(phone);
        adminValidationService.controlTheOtherAdminHasThisPhone(adminSaveDto.getPhoneNumber());
        adminValidationService.controlThePersonalNumberIsUnique(adminSaveDto.getPersonalNumber());
        Admin admin = new Admin();
        adminMapper.adminSaveDtoToAdmin(adminSaveDto, admin, phoneRepo);
        adminRepo.save(admin);
    }

    @Override
    public void updateAdmin(AdminSaveDTO adminSaveDto, Long id) {
        Admin admin = adminValidationService.returnTheAdminWithThatIdIfExists(id);
        adminValidationService.controlTheOtherAdminHasThisPersonalNumber(adminSaveDto.getPersonalNumber(), id);
        adminValidationService.controlTheOtherAdminHasThisEmail(adminSaveDto.getEmail(), id);
        adminValidationService.controlTheOtherAdminHasThisPhone(adminSaveDto.getPhoneNumber(), id);
        adminMapper.adminSaveDtoToAdmin(adminSaveDto, admin, phoneRepo);
        adminRepo.save(admin);
    }

    @Override
    public AdminViewDTO getAdminById(Long adminId) {
        Admin admin = adminValidationService.returnTheAdminWithThatIdIfExists(adminId);
        return adminMapper.adminToAdminViewDTO(admin);
    }

    @Override
    public AdminViewDTO getAdminByPhone(String phoneNumber) {
        Admin admin =  adminValidationService.returnTheAdminWithThatPhoneIfExists(phoneNumber);
        return adminMapper.adminToAdminViewDTO(admin);
    }

    @Override
    public AdminViewDTO getAdminByPersonalNumber(String personalNumber) {
        Admin admin =  adminValidationService.returnAdminWithThatPersonalNumberIfExists(personalNumber);
        return adminMapper.adminToAdminViewDTO(admin);
    }

    @Override
    public AdminViewDTO getAdminByEmail(String email) {
        Admin admin =  adminValidationService.returnTheAdminWithThatEmailIfExists(email);
        return adminMapper.adminToAdminViewDTO(admin);
    }

    @Override
    public List<AdminViewDTO> getAllAdmins() {
        List<Admin> adminList = adminRepo.findAll();
        return adminMapper.adminsToAdminsViewDTO(adminList);
    }

    @Override
    public void saveNewCustomer(CustomerSaveDTO customerSaveDto) {
        customerValidationService.controlThePersonalNumberIsUnique(customerSaveDto.getPersonalNumber());
        customerValidationService.controlTheEmailIsUnique(customerSaveDto.getEmail());
        Customer customer = new Customer();
        customerMapper.customerSaveDtoToCustomer(customerSaveDto, customer);
        customerRepo.save(customer);
    }

    @Override
    public void updateCustomer(CustomerSaveDTO customerSaveDto, Long id) {
        Customer customer = customerValidationService.returnTheCustomerWithThatIdIfExists(id);
        customerValidationService.controlTheOtherCustomerHasThisPersonalNumber(customerSaveDto.getPersonalNumber(), id);
        customerValidationService.controlTheOtherCustomerHasThisEmail(customerSaveDto.getEmail(), id);
        customerMapper.customerSaveDtoToCustomer(customerSaveDto, customer);
        customerRepo.save(customer);
    }

    @Override
    public CustomerViewDTO getCustomerById(Long customerId) {
        Customer customer = customerValidationService.returnTheCustomerWithThatIdIfExists(customerId);
        return customerMapper.customerToCustomerViewDTO(customer);
    }

    @Override
    public CustomerViewDTO getCustomerByPhone(String phoneNumber) {
        Phone phone = phoneValidationService.returnThePhoneIfExists(phoneNumber);
        phoneValidationService.controlThisPhoneHasCustomersPackageCode(phone);
        Customer customer = customerValidationService.returnTheCustomerWithThisPhoneIfExists(phone, "phone number");
        return customerMapper.customerToCustomerViewDTO(customer);
    }

    @Override
    public CustomerViewDTO getCustomerByPersonalNumber(String personalNumber) {
        Customer customer = customerValidationService.returnTheCustomerWithThatPersonalNumberIfExists(personalNumber);
        return customerMapper.customerToCustomerViewDTO(customer);
    }

    @Override
    public CustomerViewDTO getCustomerByEmail(String email) {
        Customer customer = customerValidationService.returnTheCustomerWithThatEmailIfExists(email);
        return customerMapper.customerToCustomerViewDTO(customer);
    }

    @Override
    public List<CustomerViewDTO> getAllCustomers() {
        List<Customer> customerList = customerRepo.findAll();
        return customerMapper.customersToCustomersViewDTO(customerList);
    }

    @Override
    public CustomerViewDTO getCustomerByUsername(String username) {
        User user = userValidationService.returnTheUserWithThisUsernameIfExists(username);
        userValidationService.checkIfUserIsCustomer(user);
        Phone phone = user.getPhone();
        Customer customer = customerValidationService.returnTheCustomerWithThisPhoneIfExists(phone, "username");
        return customerMapper.customerToCustomerViewDTO(customer);
    }

    @Override
    public void addPhoneToCustomer(Long customerId, String phoneNumber) {
       Customer customer = customerValidationService.returnTheCustomerWithThatIdIfExists(customerId);
       Phone phone = phoneValidationService.returnThePhoneIfExists(phoneNumber);
       phoneValidationService.controlThisPhoneHasCustomersPackageCode(phone);
       customerValidationService.controlThatPhoneAlreadyBelongsToSomeCustomer(phoneNumber);
       customer.getPhones().add(phone);
    }

    @Override
    public void removePhoneFromCustomer(Long customerId, String phoneNumber) {
        Customer customer = customerValidationService.returnTheCustomerWithThatIdIfExists(customerId);
        Phone phone = phoneValidationService.returnThePhoneIfExists(phoneNumber);
        customerValidationService.checkThatPhoneBelongsToCustomerWithThisId(customer, phone);
        customer.getPhones().remove(phone);
    }

}
