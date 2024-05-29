package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.AdminSaveDTO;
import com.snezana.introtelecom.dto.AdminViewDTO;
import com.snezana.introtelecom.dto.CustomerSaveDTO;
import com.snezana.introtelecom.dto.CustomerViewDTO;
import com.snezana.introtelecom.entity.Admin;
import com.snezana.introtelecom.entity.Customer;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.repository.AdminRepo;
import com.snezana.introtelecom.repository.CustomerRepo;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdminCustomerServiceIntegrationTest {

    @Autowired
    private AdminCustomerService adminCustomerService;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Test
    @Sql(scripts = {"/create_admin_phone.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/delete_admin.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = {"/delete_admin_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveNewAdmin(){
        int sizeBefore = adminRepo.findAll().size();
        String personalNumber = "2934387551283";
        String firstName = "Jovana";
        String lastName = "Lukić";
        String email = "joki@greenphone.com";
        String phoneNumber = "0770000077";
        AdminSaveDTO adminSaveDTO = new AdminSaveDTO();
        adminSaveDTO.setPersonalNumber(personalNumber);
        adminSaveDTO.setFirstName(firstName);
        adminSaveDTO.setLastName(lastName);
        adminSaveDTO.setEmail(email);
        adminSaveDTO.setPhoneNumber(phoneNumber);
        adminCustomerService.saveNewAdmin(adminSaveDTO);
        Admin admin = adminRepo.findAll().get(sizeBefore);
        assertThat(adminRepo.findAll().size()).isEqualTo(sizeBefore + 1);
        assertThat(admin.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(admin.getEmail()).isEqualTo(email);
        assertThat(admin.getAdminId()).isEqualTo(adminRepo.maxAdmin_id());
    }

    @Test
    @Sql(scripts = {"/update_admin.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testUpdateAdmin(){
        Long id = 1L;
        String personalNumber = "0720123763";
        String firstName = "Mihailo";
        String lastName = "Maksimović";
        String email = "mika@introtelecom1.com";
        String phoneNumber = "0770000001";
        AdminSaveDTO adminSaveDTO = new AdminSaveDTO();
        adminSaveDTO.setPersonalNumber(personalNumber);
        adminSaveDTO.setFirstName(firstName);
        adminSaveDTO.setLastName(lastName);
        adminSaveDTO.setEmail(email);
        adminSaveDTO.setPhoneNumber(phoneNumber);
        adminCustomerService.updateAdmin(adminSaveDTO, id);
        Optional<Admin> adminOpt = adminRepo.findByAdminIdOpt(id);
        assertThat(adminOpt).isPresent();
        assertThat(adminOpt).isNotEmpty();
        assertThat(adminOpt.get().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(adminOpt.get().getFirstName()).isEqualTo(firstName);
        assertThat(adminOpt.get().getLastName()).isEqualTo(lastName);
        assertThat(adminOpt.get().getEmail()).isEqualTo(email);
        assertThat(adminOpt.get().getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetAdminById(){
        Long id = 1L;
        String personalNumber = "9283478122";
        String firstName = "Mihailo";
        String lastName = "Maksić";
        String email = "mika@introtelecom.com";
        String phoneNumber = "0770000001";
        AdminViewDTO adminViewDTO = adminCustomerService.getAdminById(id);
        assertThat(adminViewDTO).isNotNull();
        assertThat(adminViewDTO.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(adminViewDTO.getFirstName()).isEqualTo(firstName);
        assertThat(adminViewDTO.getLastName()).isEqualTo(lastName);
        assertThat(adminViewDTO.getEmail()).isEqualTo(email);
        assertThat(adminViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetAdminByPhone(){
        String personalNumber = "9283478122";
        String firstName = "Mihailo";
        String lastName = "Maksić";
        String email = "mika@introtelecom.com";
        String phoneNumber = "0770000001";
        AdminViewDTO adminViewDTO = adminCustomerService.getAdminByPhone(phoneNumber);
        assertThat(adminViewDTO).isNotNull();
        assertThat(adminViewDTO.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(adminViewDTO.getFirstName()).isEqualTo(firstName);
        assertThat(adminViewDTO.getLastName()).isEqualTo(lastName);
        assertThat(adminViewDTO.getEmail()).isEqualTo(email);
        assertThat(adminViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetAdminByPersonalNumber(){
        String personalNumber = "9283478122";
        String firstName = "Mihailo";
        String lastName = "Maksić";
        String email = "mika@introtelecom.com";
        String phoneNumber = "0770000001";
        AdminViewDTO adminViewDTO = adminCustomerService.getAdminByPersonalNumber(personalNumber);
        assertThat(adminViewDTO).isNotNull();
        assertThat(adminViewDTO.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(adminViewDTO.getFirstName()).isEqualTo(firstName);
        assertThat(adminViewDTO.getLastName()).isEqualTo(lastName);
        assertThat(adminViewDTO.getEmail()).isEqualTo(email);
        assertThat(adminViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetAdminByEmail(){
        String personalNumber = "9283478122";
        String firstName = "Mihailo";
        String lastName = "Maksić";
        String email = "mika@introtelecom.com";
        String phoneNumber = "0770000001";
        AdminViewDTO adminViewDTO = adminCustomerService.getAdminByEmail(email);
        assertThat(adminViewDTO).isNotNull();
        assertThat(adminViewDTO.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(adminViewDTO.getFirstName()).isEqualTo(firstName);
        assertThat(adminViewDTO.getLastName()).isEqualTo(lastName);
        assertThat(adminViewDTO.getEmail()).isEqualTo(email);
        assertThat(adminViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetAllAdmins(){
        int size = 2;
        List<AdminViewDTO> adminViewDTOList = adminCustomerService.getAllAdmins();
        assertThat(adminViewDTOList).isNotEmpty();
        assertThat(adminViewDTOList.size()).isEqualTo(size);
    }

    @Test
    @Sql(scripts = {"/delete_customer.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveNewCustomer(){
        int sizeBefore = customerRepo.findAll().size();
        String personalNumber = "8293478321";
        String firstName = "Milan";
        String lastName = "Filipović";
        String email = "mile@bluephone.com";
        String address = "Vrnjačka 17, Novi Sad";
        CustomerSaveDTO customerSaveDTO = new CustomerSaveDTO();
        customerSaveDTO.setPersonalNumber(personalNumber);
        customerSaveDTO.setFirstName(firstName);
        customerSaveDTO.setLastName(lastName);
        customerSaveDTO.setEmail(email);
        customerSaveDTO.setAddress(address);
        adminCustomerService.saveNewCustomer(customerSaveDTO);
        Customer customer = customerRepo.findAll().get(sizeBefore);
        assertThat(customerRepo.findAll().size()).isEqualTo(sizeBefore+1);
        assertThat(customer.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customer.getFirstName()).isEqualTo(firstName);
        assertThat(customer.getLastName()).isEqualTo(lastName);
        assertThat(customer.getEmail()).isEqualTo(email);
        assertThat(customer.getAddress()).isEqualTo(address);
        assertThat(customer.getCustomerId()).isEqualTo(customerRepo.maxCustomer_id());
    }

    @Test
    @Sql(scripts = {"/update_customer.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testUpdateCustomer(){
        Long id = 1L;
        String personalNumber = "9344228821";
        String firstName = "Lenka";
        String lastName = "Jovanović";
        String email = "lena@greenphone.com";
        String address = "Radnička 47, Beograd";
        CustomerSaveDTO customerSaveDTO = new CustomerSaveDTO();
        customerSaveDTO.setPersonalNumber(personalNumber);
        customerSaveDTO.setFirstName(firstName);
        customerSaveDTO.setLastName(lastName);
        customerSaveDTO.setEmail(email);
        customerSaveDTO.setAddress(address);
        adminCustomerService.updateCustomer(customerSaveDTO, id);
        Optional<Customer> customerOpt = customerRepo.findByCustomerIdOpt(id);
        assertThat(customerOpt.isPresent()).isTrue();
        assertThat(customerOpt).isNotEmpty();
        assertThat(customerOpt.get().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerOpt.get().getFirstName()).isEqualTo(firstName);
        assertThat(customerOpt.get().getLastName()).isEqualTo(lastName);
        assertThat(customerOpt.get().getEmail()).isEqualTo(email);
        assertThat(customerOpt.get().getAddress()).isEqualTo(address);
    }

    @Test
    void testGetCustomerById(){
        Long id = 1L;
        String personalNumber = "3277645392";
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String address = "Radnička 35, Beograd";
        CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerById(id);
        assertThat(customerViewDTO.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerViewDTO.getFirstName()).isEqualTo(firstName);
        assertThat(customerViewDTO.getLastName()).isEqualTo(lastName);
        assertThat(customerViewDTO.getEmail()).isEqualTo(email);
        assertThat(customerViewDTO.getAddress()).isEqualTo(address);
    }

    @Test
    void testGetCustomerByPhone(){
        String phoneNumber = "0739823365";
        String personalNumber = "3277645392";
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String address = "Radnička 35, Beograd";
        CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerByPhone(phoneNumber);
        assertThat(customerViewDTO.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerViewDTO.getFirstName()).isEqualTo(firstName);
        assertThat(customerViewDTO.getLastName()).isEqualTo(lastName);
        assertThat(customerViewDTO.getEmail()).isEqualTo(email);
        assertThat(customerViewDTO.getAddress()).isEqualTo(address);
    }

    @Test
    void testGetCustomerByPersonalNumber(){
        String personalNumber = "3277645392";
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String address = "Radnička 35, Beograd";
        CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerByPersonalNumber(personalNumber);
        assertThat(customerViewDTO.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerViewDTO.getFirstName()).isEqualTo(firstName);
        assertThat(customerViewDTO.getLastName()).isEqualTo(lastName);
        assertThat(customerViewDTO.getEmail()).isEqualTo(email);
        assertThat(customerViewDTO.getAddress()).isEqualTo(address);
    }

    @Test
    void testGetCustomerByEmail(){
        String personalNumber = "3277645392";
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String address = "Radnička 35, Beograd";
        CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerByEmail(email);
        assertThat(customerViewDTO.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerViewDTO.getFirstName()).isEqualTo(firstName);
        assertThat(customerViewDTO.getLastName()).isEqualTo(lastName);
        assertThat(customerViewDTO.getEmail()).isEqualTo(email);
        assertThat(customerViewDTO.getAddress()).isEqualTo(address);
    }

    @Test
    void testGetAllCustomers(){
        int size = 14;
        List<CustomerViewDTO> customerViewDTOList = adminCustomerService.getAllCustomers();
        assertThat(customerViewDTOList).isNotEmpty();
        assertThat(customerViewDTOList.size()).isEqualTo(size);
    }

    @Test
    void testGetCustomerByUsername(){
        String username = "lana2";
        String personalNumber = "3277645392";
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String address = "Radnička 35, Beograd";
        CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerByUsername(username);
        assertThat(customerViewDTO.getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerViewDTO.getFirstName()).isEqualTo(firstName);
        assertThat(customerViewDTO.getLastName()).isEqualTo(lastName);
        assertThat(customerViewDTO.getEmail()).isEqualTo(email);
        assertThat(customerViewDTO.getAddress()).isEqualTo(address);
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CustomerAddRemovePhoneIntegrationTest {

        @Test
        @Order(1)
        @Sql(scripts = {"/create_phone.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void testAddPhoneToCustomer(){
            Long id = 1L;
            String phoneNumber = "0788995677";
            String personalNumber = "3277645392";
            String email = "lana@greenphone.com";
            adminCustomerService.addPhoneToCustomer(id, phoneNumber);
            CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerByPhone(phoneNumber);
            assertThat(customerViewDTO.getPersonalNumber()).isEqualTo(personalNumber);
            assertThat(customerViewDTO.getEmail()).isEqualTo(email);
            assertThat(customerViewDTO.getPhoneNumbers().contains(phoneNumber)).isTrue();
        }

        @Test
        @Order(2)
        @Sql(scripts = {"/delete_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void testRemovePhoneFromCustomer(){
            Long id = 1L;
            String phoneNumber = "0788995677";
            Condition<Phone> phone0788995677 = new Condition<>(
                    p -> p.getPhoneNumber().equals("0788995677") , "thatPhone0788995677");
            Condition<Phone> phone0720123763 = new Condition<>(
                    p -> p.getPhoneNumber().equals("0720123763") , "thatPhone0720123763");
            CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerByPhone(phoneNumber);
            adminCustomerService.removePhoneFromCustomer(id, phoneNumber);
            Customer newCustomer = customerRepo.findCustomerByPersonalNumberOtherWay(customerViewDTO.getPersonalNumber());
            assertThat(newCustomer.getPhones()).haveExactly(1, phone0720123763);
            assertThat(newCustomer.getPhones()).doNotHave(phone0788995677);
        }
    }

}
