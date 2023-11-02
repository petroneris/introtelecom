package com.snezana.introtelecom.services;

import com.snezana.introtelecom.dto.AdminSaveDTO;
import com.snezana.introtelecom.dto.AdminViewDTO;
import com.snezana.introtelecom.dto.CustomerSaveDTO;
import com.snezana.introtelecom.dto.CustomerViewDTO;
import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.entity.Customer;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.mapper.AdminMapper;
import com.snezana.introtelecom.mapper.CustomerMapper;
import com.snezana.introtelecom.mapper.PhoneMapper;
import com.snezana.introtelecom.mapper.UserMapper;
import com.snezana.introtelecom.repositories.AdminRepo;
import com.snezana.introtelecom.repositories.CustomerRepo;
import com.snezana.introtelecom.repositories.PhoneRepo;
import com.snezana.introtelecom.repositories.UserRepo;
import com.snezana.introtelecom.validations.AdminValidationService;
import com.snezana.introtelecom.validations.CustomerValidationService;
import com.snezana.introtelecom.validations.PhoneValidationService;
import com.snezana.introtelecom.validations.UserValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.Optional;

@Service
@Transactional
public class AdminCustomerServiceImpl implements AdminCustomerService{
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminCustomerServiceImpl.class);

    @Autowired
    PhoneRepo phoneRepo;

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    PhoneValidationService phoneValidationService;

    @Autowired
    UserValidationService userValidationService;

    @Autowired
    AdminValidationService adminValidationService;

    @Autowired
    CustomerValidationService customerValidationService;

    @Autowired
    PhoneMapper phoneMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    UserMapper userMapper;


    @Override
    public void saveNewAdmin(AdminSaveDTO adminSaveDto) {
        phoneValidationService.controlThePhoneExists(adminSaveDto.getPhoneNumber());
        phoneValidationService.controlIsTheAdminPackageCode(adminSaveDto.getPhoneNumber());
        adminValidationService.controlTheOtherAdminHasThisPhone(adminSaveDto.getPhoneNumber());
        adminValidationService.controlThePersonalNumberIsUnique(adminSaveDto.getPersonalNumber());
        Admin admin = new Admin();
        adminMapper.adminSaveDtoToAdmin(adminSaveDto, admin, phoneRepo);
        adminRepo.save(admin);
    }

    @Override
    public void updateAdmin(AdminSaveDTO adminSaveDto, Long id) {
        adminValidationService.controlTheAdminWithThatIdExists(id);
        adminValidationService.controlUpdateTheOtherAdminHasThisPersonalNumber(adminSaveDto.getPersonalNumber(), id);
        adminValidationService.controlUpdateTheOtherAdminHasThisEmail(adminSaveDto.getEmail(), id);
        adminValidationService.controlUpdateTheOtherAdminHasThisPhone(adminSaveDto.getPhoneNumber(), id);
        Admin admin = adminRepo.findByAdminId(id);
        adminMapper.adminSaveDtoToAdmin(adminSaveDto, admin, phoneRepo);
        adminRepo.save(admin);
    }

    @Override
    public AdminViewDTO getAdminById(Long adminId) {
        adminValidationService.controlTheAdminWithThatIdExists(adminId);
        Admin admin = adminRepo.findByAdminId(adminId);
        return adminMapper.adminToAdminViewDTO(admin);
    }

    @Override
    public AdminViewDTO getAdminByPhone(String phoneNumber) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        phoneValidationService.controlIsTheAdminPackageCode(phoneNumber);
        Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
        Admin admin = adminRepo.findByPhone(phone);
        return adminMapper.adminToAdminViewDTO(admin);
    }

    @Override
    public AdminViewDTO getAdminByPersonalNumber(String personalNumber) {
        adminValidationService.controlThePersonalNumberExists(personalNumber);
        Admin admin = adminRepo.findByPersonalNumber(personalNumber);
        return adminMapper.adminToAdminViewDTO(admin);
    }

    @Override
    public AdminViewDTO getAdminByEmail(String email) {
        adminValidationService.controlTheEmailExists(email);
        Admin admin = adminRepo.findAdminByEmail(email);
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
        customerValidationService.controlTheCustomerWithThatIdExists(id);
        customerValidationService.controlUpdateTheOtherCustomerHasThisPersonalNumber(customerSaveDto.getPersonalNumber(), id);
        customerValidationService.controlUpdateTheOtherCustomerHasThisEmail(customerSaveDto.getEmail(), id);
        Customer customer = customerRepo.findByCustomerId(id);
        customerMapper.customerSaveDtoToCustomer(customerSaveDto, customer);
        customerRepo.save(customer);
    }

    @Override
    public CustomerViewDTO getCustomerById(Long customerId) {
        customerValidationService.controlTheCustomerWithThatIdExists(customerId);
        Customer customer = customerRepo.findByCustomerId(customerId);
        return customerMapper.customerToCustomerViewDTO(customer);
    }

    @Override
    public CustomerViewDTO getCustomerByPhone(String phoneNumber) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        phoneValidationService.checkThatPhoneHasCustomersPackageCode(phoneNumber);
        Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
        log.info("phone is: {}", phone.getPhoneNumber());
        Optional<Customer> customerOpt = Optional.empty();
        log.info("after optional");
        List<Customer> customerList = customerRepo.findAll();
        log.info("customerList is: {}", customerList.size());
        for (Customer custm: customerList){
            Set<Phone> phones = custm.getPhones();
            log.info("phones is: {}", phones.size());
            if(phones.contains(phone)) {
                customerOpt = Optional.of(custm);
                log.info("in contains");
                break;
            }
        }
        log.info("after for");
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            log.info("customer is: {}", customer);
            return customerMapper.customerToCustomerViewDTO(customer);
        } else {
            log.info("throw");
            throw new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The customer with that phone number doesn't exist in database!");
        }
    }

    @Override
    public CustomerViewDTO getCustomerByPersonalNumber(String personalNumber) {
        customerValidationService.controlThePersonalNumberExists(personalNumber);
        Customer customer = customerRepo.findByPersonalNumber(personalNumber);
        return customerMapper.customerToCustomerViewDTO(customer);
    }

    @Override
    public CustomerViewDTO getCustomerByEmail(String email) {
        customerValidationService.controlTheEmailExists(email);
        Customer customer = customerRepo.findCustomerByEmail(email);
        return customerMapper.customerToCustomerViewDTO(customer);
    }

    @Override
    public List<CustomerViewDTO> getAllCustomers() {
        List<Customer> customerList = customerRepo.findAll();
        return customerMapper.customersToCustomersViewDTO(customerList);
    }

    @Override
    public CustomerViewDTO getCustomerByUsername(String username) {
       userValidationService.controlTheUsernameExists (username);
       userValidationService.checkIfUserIsCustomer(username);
        log.info("getCustomerByUsername");
        User user = userRepo.findByUsername(username);
        log.info("user is: {}", user.getUsername());
        Phone phone = user.getPhone();
        log.info("phone is: {}", phone.getPhoneNumber());
        Optional<Customer> customerOpt = Optional.empty();
        log.info("after optional");
        List<Customer> customerList = customerRepo.findAll();
        log.info("customerList is: {}", customerList.size());
        for (Customer custm: customerList){
            Set<Phone> phones = custm.getPhones();
            log.info("phones is: {}", phones.size());
            if(phones.contains(phone)) {
                customerOpt = Optional.of(custm);
                log.info("in contains");
                break;
            }
        }
        log.info("after for");
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            log.info("customer is: {}", customer);
            return customerMapper.customerToCustomerViewDTO(customer);
        } else {
            log.info("throw");
            throw new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The customer with that username doesn't exist in database!");
        }
    }

    @Override
    public void addPhoneToCustomer(Long customerId, String phoneNumber) {
       customerValidationService.controlTheCustomerWithThatIdExists(customerId);
       phoneValidationService.checkThatPhoneHasCustomersPackageCode(phoneNumber);
       customerValidationService.controlThatPhoneAlreadyBelongsToSomeCustomer(phoneNumber);
       Customer customer = customerRepo.findByCustomerId(customerId);
       Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
       customer.getPhones().add(phone);
    }

    @Override
    public void removePhoneFromCustomer(Long customerId, String phoneNumber) {
        customerValidationService.controlTheCustomerWithThatIdExists(customerId);
        customerValidationService.checkThatPhoneBelongsToCustomerWithThisId(customerId, phoneNumber);
        Customer customer = customerRepo.findByCustomerId(customerId);
        Phone phone = phoneRepo.findByPhoneNumber(phoneNumber);
        customer.getPhones().remove(phone);
    }
}
