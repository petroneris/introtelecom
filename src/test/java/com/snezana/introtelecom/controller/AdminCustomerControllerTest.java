package com.snezana.introtelecom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snezana.introtelecom.dto.AdminSaveDTO;
import com.snezana.introtelecom.dto.AdminViewDTO;
import com.snezana.introtelecom.dto.CustomerSaveDTO;
import com.snezana.introtelecom.dto.CustomerViewDTO;
import com.snezana.introtelecom.service.AdminCustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AdminCustomerController.class)
@WithMockUser(username ="mika", roles="ADMIN")  // to eliminate status 401(Unauthorized) and enable testing
class AdminCustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminCustomerService adminCustomerService;

    @Test
    void testSaveAdmin() throws Exception {
        String personalNumber = "9882934573234";
        String firstName = "Luka";
        String lastName = "Lukić";
        String email = "luka@introtelecom.com";
        String phoneNumber = "0730000005";
        AdminSaveDTO adminSaveDTO = new AdminSaveDTO();
        adminSaveDTO.setPersonalNumber(personalNumber);
        adminSaveDTO.setFirstName(firstName);
        adminSaveDTO.setLastName(lastName);
        adminSaveDTO.setEmail(email);
        adminSaveDTO.setPhoneNumber(phoneNumber);
        String message = "New admin is saved.";

        doNothing().when(adminCustomerService).saveNewAdmin(adminSaveDTO);

        mockMvc.perform(post("/api/admin/saveNewAdmin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminSaveDTO)))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUpdateAdmin() throws Exception {
        Long adminId = 1L;
        String personalNumber = "9882934573234";
        String firstName = "Luka";
        String lastName = "Lukić";
        String email = "luka@introtelecom.com";
        String phoneNumber = "0730000005";
        AdminSaveDTO adminSaveDTO = new AdminSaveDTO();
        adminSaveDTO.setPersonalNumber(personalNumber);
        adminSaveDTO.setFirstName(firstName);
        adminSaveDTO.setLastName(lastName);
        adminSaveDTO.setEmail(email);
        adminSaveDTO.setPhoneNumber(phoneNumber);
        String message = "The admin is updated.";

        doNothing().when(adminCustomerService).updateAdmin(adminSaveDTO, adminId);

        mockMvc.perform(put("/api/admin/updateAdmin/{adminId}", adminId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminSaveDTO)))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAdminById() throws Exception {
        Long adminId = 1L;
        String personalNumber = "9882934573234";
        String firstName = "Luka";
        String lastName = "Lukić";
        String email = "luka@introtelecom.com";
        String phoneNumber = "0730000005";
        AdminViewDTO adminViewDTO = new AdminViewDTO();
        adminViewDTO.setAdminId(adminId);
        adminViewDTO.setPersonalNumber(personalNumber);
        adminViewDTO.setFirstName(firstName);
        adminViewDTO.setLastName(lastName);
        adminViewDTO.setEmail(email);
        adminViewDTO.setPhoneNumber(phoneNumber);

        when(adminCustomerService.getAdminById(adminId)).thenReturn(adminViewDTO);

        mockMvc.perform(get("/api/admin/getAdminById/{adminId}", adminId))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.personalNumber").value(personalNumber))
                .andExpect(jsonPath("$.data.firstName").value(firstName))
                .andExpect(jsonPath("$.data.lastName").value(lastName))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.adminId").value(adminId))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAdminByPhoneNumber() throws Exception {
        String personalNumber = "9882934573234";
        String firstName = "Luka";
        String lastName = "Lukić";
        String email = "luka@introtelecom.com";
        String phoneNumber = "0730000005";
        AdminViewDTO adminViewDTO = new AdminViewDTO();
        adminViewDTO.setPersonalNumber(personalNumber);
        adminViewDTO.setFirstName(firstName);
        adminViewDTO.setLastName(lastName);
        adminViewDTO.setEmail(email);
        adminViewDTO.setPhoneNumber(phoneNumber);

        when(adminCustomerService.getAdminByPhone(phoneNumber)).thenReturn(adminViewDTO);

        mockMvc.perform(get("/api/admin/getAdminByPhoneNumber/{phoneNumber}", phoneNumber))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.personalNumber").value(personalNumber))
                .andExpect(jsonPath("$.data.firstName").value(firstName))
                .andExpect(jsonPath("$.data.lastName").value(lastName))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAdminByPersonalNumber() throws Exception {
        String personalNumber = "9882934573234";
        String firstName = "Luka";
        String lastName = "Lukić";
        String email = "luka@introtelecom.com";
        String phoneNumber = "0730000005";
        AdminViewDTO adminViewDTO = new AdminViewDTO();
        adminViewDTO.setPersonalNumber(personalNumber);
        adminViewDTO.setFirstName(firstName);
        adminViewDTO.setLastName(lastName);
        adminViewDTO.setEmail(email);
        adminViewDTO.setPhoneNumber(phoneNumber);

        when(adminCustomerService.getAdminByPersonalNumber(personalNumber)).thenReturn(adminViewDTO);

        mockMvc.perform(get("/api/admin/getAdminByPersonalNumber/{personalNumber}", personalNumber))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.personalNumber").value(personalNumber))
                .andExpect(jsonPath("$.data.firstName").value(firstName))
                .andExpect(jsonPath("$.data.lastName").value(lastName))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAdminByEmail() throws Exception {
        String personalNumber = "9882934573234";
        String firstName = "Luka";
        String lastName = "Lukić";
        String email = "luka@introtelecom.com";
        String phoneNumber = "0730000005";
        AdminViewDTO adminViewDTO = new AdminViewDTO();
        adminViewDTO.setPersonalNumber(personalNumber);
        adminViewDTO.setFirstName(firstName);
        adminViewDTO.setLastName(lastName);
        adminViewDTO.setEmail(email);
        adminViewDTO.setPhoneNumber(phoneNumber);

        when(adminCustomerService.getAdminByEmail(email)).thenReturn(adminViewDTO);

        mockMvc.perform(get("/api/admin/getAdminByEmail/{email}", email))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.personalNumber").value(personalNumber))
                .andExpect(jsonPath("$.data.firstName").value(firstName))
                .andExpect(jsonPath("$.data.lastName").value(lastName))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllAdmins() throws Exception {
        String personalNumber1 = "9882934573234";
        String firstName1 = "Luka";
        String lastName1 = "Lukić";
        String email1 = "luka@introtelecom.com";
        String phoneNumber1 = "0730000005";
        AdminViewDTO adminViewDTO1 = new AdminViewDTO();
        adminViewDTO1.setPersonalNumber(personalNumber1);
        adminViewDTO1.setFirstName(firstName1);
        adminViewDTO1.setLastName(lastName1);
        adminViewDTO1.setEmail(email1);
        adminViewDTO1.setPhoneNumber(phoneNumber1);
        String personalNumber2 = "9882934573567";
        String firstName2 = "Iva";
        String lastName2 = "Ivić";
        String email2 = "iva@introtelecom.com";
        String phoneNumber2 = "0730000007";
        AdminViewDTO adminViewDTO2 = new AdminViewDTO();
        adminViewDTO2.setPersonalNumber(personalNumber2);
        adminViewDTO2.setFirstName(firstName2);
        adminViewDTO2.setLastName(lastName2);
        adminViewDTO2.setEmail(email2);
        adminViewDTO2.setPhoneNumber(phoneNumber2);
        List<AdminViewDTO> adminViewDTOList = new ArrayList<>();
        adminViewDTOList.add(adminViewDTO1);
        adminViewDTOList.add(adminViewDTO2);

        when(adminCustomerService.getAllAdmins()).thenReturn(adminViewDTOList);

        mockMvc.perform(get("/api/admin/getAllAdmins"))
                .andExpect(jsonPath("$.data.size()").value(adminViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testSaveCustomer() throws Exception {
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
        String message = "New customer is saved.";

        doNothing().when(adminCustomerService).saveNewCustomer(customerSaveDTO);

        mockMvc.perform(post("/api/customer/saveNewCustomer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerSaveDTO)))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Long customerId = 1L;
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
        String message = "The customer is updated.";

        doNothing().when(adminCustomerService).updateCustomer(customerSaveDTO, customerId);

        mockMvc.perform(put("/api/customer/updateCustomer/{customerId}", customerId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerSaveDTO)))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetCustomerById() throws Exception {
        Long customerId = 1L;
        String phoneNumber1 = "0738873645";
        String phoneNumber2 = "0738873445";
        String personalNumber = "8293478321";
        String firstName = "Milan";
        String lastName = "Filipović";
        String email = "mile@bluephone.com";
        String address = "Vrnjačka 17, Novi Sad";
        CustomerViewDTO customerViewDTO = new CustomerViewDTO();
        customerViewDTO.setPersonalNumber(personalNumber);
        customerViewDTO.setFirstName(firstName);
        customerViewDTO.setLastName(lastName);
        customerViewDTO.setEmail(email);
        customerViewDTO.setAddress(address);
        Set<String> phoneNumbers = new HashSet<>();
        phoneNumbers.add(phoneNumber1);
        phoneNumbers.add(phoneNumber2);
        customerViewDTO.setPhoneNumbers(phoneNumbers);

        when(adminCustomerService.getCustomerById(customerId)).thenReturn(customerViewDTO);

        mockMvc.perform(get("/api/customer/getCustomerById/{customerId}", customerId))
                .andExpect(jsonPath("$.data.phoneNumbers[0]").value(phoneNumber2))
                .andExpect(jsonPath("$.data.phoneNumbers[1]").value(phoneNumber1))
                .andExpect(jsonPath("$.data.personalNumber").value(personalNumber))
                .andExpect(jsonPath("$.data.firstName").value(firstName))
                .andExpect(jsonPath("$.data.lastName").value(lastName))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetCustomerByPersonalNumber() throws Exception {
        String personalNumber = "8293478321";
        String phoneNumber1 = "0738873645";
        String phoneNumber2 = "0738873445";
        String firstName = "Milan";
        String lastName = "Filipović";
        String email = "mile@bluephone.com";
        String address = "Vrnjačka 17, Novi Sad";
        CustomerViewDTO customerViewDTO = new CustomerViewDTO();
        customerViewDTO.setPersonalNumber(personalNumber);
        customerViewDTO.setFirstName(firstName);
        customerViewDTO.setLastName(lastName);
        customerViewDTO.setEmail(email);
        customerViewDTO.setAddress(address);
        Set<String> phoneNumbers = new HashSet<>();
        phoneNumbers.add(phoneNumber1);
        phoneNumbers.add(phoneNumber2);
        customerViewDTO.setPhoneNumbers(phoneNumbers);

        when(adminCustomerService.getCustomerByPersonalNumber(personalNumber)).thenReturn(customerViewDTO);

        mockMvc.perform(get("/api/customer/getCustomerByPersonalNumber/{personalNumber}", personalNumber))
                .andExpect(jsonPath("$.data.phoneNumbers[0]").value(phoneNumber2))
                .andExpect(jsonPath("$.data.phoneNumbers[1]").value(phoneNumber1))
                .andExpect(jsonPath("$.data.personalNumber").value(personalNumber))
                .andExpect(jsonPath("$.data.firstName").value(firstName))
                .andExpect(jsonPath("$.data.lastName").value(lastName))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetCustomerByEmail() throws Exception {
        String email = "mile@bluephone.com";
        String personalNumber = "8293478321";
        String phoneNumber1 = "0738873645";
        String phoneNumber2 = "0738873445";
        String firstName = "Milan";
        String lastName = "Filipović";
        String address = "Vrnjačka 17, Novi Sad";
        CustomerViewDTO customerViewDTO = new CustomerViewDTO();
        customerViewDTO.setPersonalNumber(personalNumber);
        customerViewDTO.setFirstName(firstName);
        customerViewDTO.setLastName(lastName);
        customerViewDTO.setEmail(email);
        customerViewDTO.setAddress(address);
        Set<String> phoneNumbers = new HashSet<>();
        phoneNumbers.add(phoneNumber1);
        phoneNumbers.add(phoneNumber2);
        customerViewDTO.setPhoneNumbers(phoneNumbers);

        when(adminCustomerService.getCustomerByEmail(email)).thenReturn(customerViewDTO);

        mockMvc.perform(get("/api/customer/getCustomerByEmail/{email}", email))
                .andExpect(jsonPath("$.data.phoneNumbers[0]").value(phoneNumber2))
                .andExpect(jsonPath("$.data.phoneNumbers[1]").value(phoneNumber1))
                .andExpect(jsonPath("$.data.personalNumber").value(personalNumber))
                .andExpect(jsonPath("$.data.firstName").value(firstName))
                .andExpect(jsonPath("$.data.lastName").value(lastName))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        String personalNumber1 = "8293478321";
        String firstName1 = "Milan";
        String lastName1 = "Filipović";
        String email1 = "mile@bluephone.com";
        String address1 = "Vrnjačka 17, Novi Sad";
        CustomerViewDTO customerViewDTO1 = new CustomerViewDTO();
        customerViewDTO1.setPersonalNumber(personalNumber1);
        customerViewDTO1.setFirstName(firstName1);
        customerViewDTO1.setLastName(lastName1);
        customerViewDTO1.setEmail(email1);
        customerViewDTO1.setAddress(address1);
        String personalNumber2 = "3277645392";
        String firstName2 = "Lana";
        String lastName2 = "Jovanović";
        String email2 = "lana@greenphone.com";
        String address2 = "Radnička 35, Beograd";
        CustomerViewDTO customerViewDTO2 = new CustomerViewDTO();
        customerViewDTO2.setPersonalNumber(personalNumber2);
        customerViewDTO2.setFirstName(firstName2);
        customerViewDTO2.setLastName(lastName2);
        customerViewDTO2.setEmail(email2);
        customerViewDTO2.setAddress(address2);
        List<CustomerViewDTO> customerViewDTOList = new ArrayList<>();
        customerViewDTOList.add(customerViewDTO1);
        customerViewDTOList.add(customerViewDTO2);

        when(adminCustomerService.getAllCustomers()).thenReturn(customerViewDTOList);

        mockMvc.perform(get("/api/customer/getAllCustomers"))
                .andExpect(jsonPath("$.data.size()").value(customerViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetCustomerByUsername() throws Exception {
        String username = "mile1";
        String email = "mile@bluephone.com";
        String personalNumber = "8293478321";
        String phoneNumber1 = "0738873645";
        String phoneNumber2 = "0738873445";
        String firstName = "Milan";
        String lastName = "Filipović";
        String address = "Vrnjačka 17, Novi Sad";
        CustomerViewDTO customerViewDTO = new CustomerViewDTO();
        customerViewDTO.setPersonalNumber(personalNumber);
        customerViewDTO.setFirstName(firstName);
        customerViewDTO.setLastName(lastName);
        customerViewDTO.setEmail(email);
        customerViewDTO.setAddress(address);
        Set<String> phoneNumbers = new HashSet<>();
        phoneNumbers.add(phoneNumber1);
        phoneNumbers.add(phoneNumber2);
        customerViewDTO.setPhoneNumbers(phoneNumbers);

        when(adminCustomerService.getCustomerByUsername(username)).thenReturn(customerViewDTO);

        mockMvc.perform(get("/api/customer/getCustomerByUsername/{username}", username))
                .andExpect(jsonPath("$.data.phoneNumbers[0]").value(phoneNumber2))
                .andExpect(jsonPath("$.data.phoneNumbers[1]").value(phoneNumber1))
                .andExpect(jsonPath("$.data.personalNumber").value(personalNumber))
                .andExpect(jsonPath("$.data.firstName").value(firstName))
                .andExpect(jsonPath("$.data.lastName").value(lastName))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetCustomerByPhone() throws Exception {
        String email = "mile@bluephone.com";
        String personalNumber = "8293478321";
        String phoneNumber1 = "0738873645";
        String phoneNumber2 = "0738873445";
        String firstName = "Milan";
        String lastName = "Filipović";
        String address = "Vrnjačka 17, Novi Sad";
        CustomerViewDTO customerViewDTO = new CustomerViewDTO();
        customerViewDTO.setPersonalNumber(personalNumber);
        customerViewDTO.setFirstName(firstName);
        customerViewDTO.setLastName(lastName);
        customerViewDTO.setEmail(email);
        customerViewDTO.setAddress(address);
        Set<String> phoneNumbers = new HashSet<>();
        phoneNumbers.add(phoneNumber1);
        phoneNumbers.add(phoneNumber2);
        customerViewDTO.setPhoneNumbers(phoneNumbers);

        when(adminCustomerService.getCustomerByPhone(phoneNumber1)).thenReturn(customerViewDTO);

        mockMvc.perform(get("/api/customer/getCustomerByPhone/{phoneNumber}", phoneNumber1))
                .andExpect(jsonPath("$.data.phoneNumbers[0]").value(phoneNumber2))
                .andExpect(jsonPath("$.data.phoneNumbers[1]").value(phoneNumber1))
                .andExpect(jsonPath("$.data.personalNumber").value(personalNumber))
                .andExpect(jsonPath("$.data.firstName").value(firstName))
                .andExpect(jsonPath("$.data.lastName").value(lastName))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testAddPhoneToCustomer() throws Exception {
        Long customerId = 1L;
        String phoneNumber = "0738873645";
        String email = "mile@bluephone.com";
        String personalNumber = "8293478321";
        String firstName = "Milan";
        String lastName = "Filipović";
        String address = "Vrnjačka 17, Novi Sad";
        CustomerViewDTO customerViewDTO = new CustomerViewDTO();
        customerViewDTO.setPersonalNumber(personalNumber);
        customerViewDTO.setFirstName(firstName);
        customerViewDTO.setLastName(lastName);
        customerViewDTO.setEmail(email);
        customerViewDTO.setAddress(address);
        String message = "The phone is added to customer.";

        doNothing().when(adminCustomerService).addPhoneToCustomer(customerId, phoneNumber);

        mockMvc.perform(patch("/api/customer/addPhoneToCustomer/{customerId}", customerId).param("phoneNumber", phoneNumber)
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testRemovePhoneFromCustomer() throws Exception {
        Long customerId = 1L;
        String phoneNumber = "0738873645";
        String email = "mile@bluephone.com";
        String personalNumber = "8293478321";
        String firstName = "Milan";
        String lastName = "Filipović";
        String address = "Vrnjačka 17, Novi Sad";
        CustomerViewDTO customerViewDTO = new CustomerViewDTO();
        customerViewDTO.setPersonalNumber(personalNumber);
        customerViewDTO.setFirstName(firstName);
        customerViewDTO.setLastName(lastName);
        customerViewDTO.setEmail(email);
        customerViewDTO.setAddress(address);
        String message = "The phone is removed from customer.";

        doNothing().when(adminCustomerService).removePhoneFromCustomer(customerId, phoneNumber);

        mockMvc.perform(patch("/api/customer/removePhoneFromCustomer/{customerId}", customerId).param("phoneNumber", phoneNumber)
                        .with(csrf()))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }
}