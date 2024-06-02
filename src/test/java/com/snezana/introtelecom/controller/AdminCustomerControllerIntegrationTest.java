package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.security.JWTtokenGenerator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminCustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    JWTtokenGenerator jwTtokenGenerator;

    @Autowired
    AuthenticationManager authenticationManager;

    String access_token;

    @BeforeEach
    void setupUser(){
        String username = "mika"; // ADMIN
        String password = "mika";
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        access_token = jwTtokenGenerator.generateAccess_Token(authentication);
    }

    @Test
    @Sql(scripts = {"/create_admin_phone.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/delete_admin.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = {"/delete_admin_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveAdminIT(){
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
        String message = "New admin is saved.";

        String url = "http://localhost:" + port + "/api/admin/saveNewAdmin";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<AdminSaveDTO> requestEntity = new HttpEntity<>( adminSaveDTO, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
                });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    @Sql(scripts = {"/update_admin.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testUpdateAdminIT(){
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
        String message = "The admin is updated.";

        String url = "http://localhost:" + port + "/api/admin/updateAdmin/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<AdminSaveDTO> requestEntity = new HttpEntity<>( adminSaveDTO, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.PUT, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
                });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    void testGetAdminByIdIT(){
        Long id = 1L;
        String personalNumber = "9283478122";
        String firstName = "Mihailo";
        String lastName = "Maksić";
        String email = "mika@introtelecom.com";
        String phoneNumber = "0770000001";

        String url = "http://localhost:" + port + "/api/admin/getAdminById/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<AdminViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<AdminViewDTO>>() {
                });

        RestAPIResponse<AdminViewDTO> adminViewDTOResponse = responseEntity.getBody();
        assertThat(adminViewDTOResponse).isNotNull();
        assertThat(adminViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(adminViewDTOResponse.isSuccess()).isTrue();
        assertThat(adminViewDTOResponse.getData().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(adminViewDTOResponse.getData().getFirstName()).isEqualTo(firstName);
        assertThat(adminViewDTOResponse.getData().getLastName()).isEqualTo(lastName);
        assertThat(adminViewDTOResponse.getData().getEmail()).isEqualTo(email);
        assertThat(adminViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetAdminByPhoneNumberIT(){
        Long id = 1L;
        String phoneNumber = "0770000001";
        String personalNumber = "9283478122";
        String firstName = "Mihailo";
        String lastName = "Maksić";
        String email = "mika@introtelecom.com";

        String url = "http://localhost:" + port + "/api/admin/getAdminByPhoneNumber/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<AdminViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<AdminViewDTO>>() {
                });

        RestAPIResponse<AdminViewDTO> adminViewDTOResponse = responseEntity.getBody();
        assertThat(adminViewDTOResponse).isNotNull();
        assertThat(adminViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(adminViewDTOResponse.isSuccess()).isTrue();
        assertThat(adminViewDTOResponse.getData().getAdminId()).isEqualTo(id);
        assertThat(adminViewDTOResponse.getData().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(adminViewDTOResponse.getData().getFirstName()).isEqualTo(firstName);
        assertThat(adminViewDTOResponse.getData().getLastName()).isEqualTo(lastName);
        assertThat(adminViewDTOResponse.getData().getEmail()).isEqualTo(email);
        assertThat(adminViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetAdminByPersonalNumberIT(){
        Long id = 1L;
        String phoneNumber = "0770000001";
        String personalNumber = "9283478122";
        String firstName = "Mihailo";
        String lastName = "Maksić";
        String email = "mika@introtelecom.com";

        String url = "http://localhost:" + port + "/api/admin/getAdminByPersonalNumber/" + personalNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<AdminViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<AdminViewDTO>>() {
                });

        RestAPIResponse<AdminViewDTO> adminViewDTOResponse = responseEntity.getBody();
        assertThat(adminViewDTOResponse).isNotNull();
        assertThat(adminViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(adminViewDTOResponse.isSuccess()).isTrue();
        assertThat(adminViewDTOResponse.getData().getAdminId()).isEqualTo(id);
        assertThat(adminViewDTOResponse.getData().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(adminViewDTOResponse.getData().getFirstName()).isEqualTo(firstName);
        assertThat(adminViewDTOResponse.getData().getLastName()).isEqualTo(lastName);
        assertThat(adminViewDTOResponse.getData().getEmail()).isEqualTo(email);
        assertThat(adminViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetAdminByEmailIT(){
        Long id = 1L;
        String phoneNumber = "0770000001";
        String personalNumber = "9283478122";
        String firstName = "Mihailo";
        String lastName = "Maksić";
        String email = "mika@introtelecom.com";

        String url = "http://localhost:" + port + "/api/admin/getAdminByEmail/" + email;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<AdminViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<AdminViewDTO>>() {
                });

        RestAPIResponse<AdminViewDTO> adminViewDTOResponse = responseEntity.getBody();
        assertThat(adminViewDTOResponse).isNotNull();
        assertThat(adminViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(adminViewDTOResponse.isSuccess()).isTrue();
        assertThat(adminViewDTOResponse.getData().getAdminId()).isEqualTo(id);
        assertThat(adminViewDTOResponse.getData().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(adminViewDTOResponse.getData().getFirstName()).isEqualTo(firstName);
        assertThat(adminViewDTOResponse.getData().getLastName()).isEqualTo(lastName);
        assertThat(adminViewDTOResponse.getData().getEmail()).isEqualTo(email);
        assertThat(adminViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetAllAdminsIT(){
        int listSize = 2;

        String url = "http://localhost:" + port + "/api/admin/getAllAdmins";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<List<AdminViewDTO>>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<AdminViewDTO>>>() {
                });

        RestAPIResponse<List<AdminViewDTO>> adminViewDTOListResponse = responseEntity.getBody();
        assertThat(adminViewDTOListResponse).isNotNull();
        assertThat(adminViewDTOListResponse.isSuccess()).isTrue();
        assertThat(adminViewDTOListResponse.getData().size()).isEqualTo(listSize);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Sql(scripts = {"/delete_customer.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveCustomerIT(){
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

        String url = "http://localhost:" + port + "/api/customer/saveNewCustomer";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<CustomerSaveDTO> requestEntity = new HttpEntity<>(customerSaveDTO, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
                });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    @Sql(scripts = {"/update_customer.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testUpdateCustomerIT(){
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
        String message = "The customer is updated.";

        String url = "http://localhost:" + port + "/api/customer/updateCustomer/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<CustomerSaveDTO> requestEntity = new HttpEntity<>( customerSaveDTO, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.PUT, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
                });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    void testGetCustomerByIdIT(){
        Long id = 1L;
        String personalNumber = "3277645392";
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String address = "Radnička 35, Beograd";

        String url = "http://localhost:" + port + "/api/customer/getCustomerById/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<CustomerViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<CustomerViewDTO>>() {
                });

        RestAPIResponse<CustomerViewDTO> customerViewDTOResponse = responseEntity.getBody();
        assertThat(customerViewDTOResponse).isNotNull();
        assertThat(customerViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(customerViewDTOResponse.isSuccess()).isTrue();
        assertThat(customerViewDTOResponse.getData().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerViewDTOResponse.getData().getFirstName()).isEqualTo(firstName);
        assertThat(customerViewDTOResponse.getData().getLastName()).isEqualTo(lastName);
        assertThat(customerViewDTOResponse.getData().getEmail()).isEqualTo(email);
        assertThat(customerViewDTOResponse.getData().getAddress()).isEqualTo(address);
    }

    @Test
    void testGetCustomerByPersonalNumberIT(){
        String personalNumber = "3277645392";
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String address = "Radnička 35, Beograd";

        String url = "http://localhost:" + port + "/api/customer/getCustomerByPersonalNumber/" + personalNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<CustomerViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<CustomerViewDTO>>() {
                });

        RestAPIResponse<CustomerViewDTO> customerViewDTOResponse = responseEntity.getBody();
        assertThat(customerViewDTOResponse).isNotNull();
        assertThat(customerViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(customerViewDTOResponse.isSuccess()).isTrue();
        assertThat(customerViewDTOResponse.getData().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerViewDTOResponse.getData().getFirstName()).isEqualTo(firstName);
        assertThat(customerViewDTOResponse.getData().getLastName()).isEqualTo(lastName);
        assertThat(customerViewDTOResponse.getData().getEmail()).isEqualTo(email);
        assertThat(customerViewDTOResponse.getData().getAddress()).isEqualTo(address);
    }

    @Test
    void testGetCustomerByEmailIT(){
        String personalNumber = "3277645392";
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String address = "Radnička 35, Beograd";

        String url = "http://localhost:" + port + "/api/customer/getCustomerByEmail/" + email;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<CustomerViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<CustomerViewDTO>>() {
                });

        RestAPIResponse<CustomerViewDTO> customerViewDTOResponse = responseEntity.getBody();
        assertThat(customerViewDTOResponse).isNotNull();
        assertThat(customerViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(customerViewDTOResponse.isSuccess()).isTrue();
        assertThat(customerViewDTOResponse.getData().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerViewDTOResponse.getData().getFirstName()).isEqualTo(firstName);
        assertThat(customerViewDTOResponse.getData().getLastName()).isEqualTo(lastName);
        assertThat(customerViewDTOResponse.getData().getEmail()).isEqualTo(email);
        assertThat(customerViewDTOResponse.getData().getAddress()).isEqualTo(address);
    }

    @Test
    void testGetCustomerByUsernameIT(){
        String username = "lana2";
        String personalNumber = "3277645392";
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String address = "Radnička 35, Beograd";

        String url = "http://localhost:" + port + "/api/customer/getCustomerByUsername/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<CustomerViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<CustomerViewDTO>>() {
                });

        RestAPIResponse<CustomerViewDTO> customerViewDTOResponse = responseEntity.getBody();
        assertThat(customerViewDTOResponse).isNotNull();
        assertThat(customerViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(customerViewDTOResponse.isSuccess()).isTrue();
        assertThat(customerViewDTOResponse.getData().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerViewDTOResponse.getData().getFirstName()).isEqualTo(firstName);
        assertThat(customerViewDTOResponse.getData().getLastName()).isEqualTo(lastName);
        assertThat(customerViewDTOResponse.getData().getEmail()).isEqualTo(email);
        assertThat(customerViewDTOResponse.getData().getAddress()).isEqualTo(address);
    }

    @Test
    void testGetCustomerByPhoneIT(){
        String phoneNumber = "0739823365";
        String personalNumber = "3277645392";
        String firstName = "Lana";
        String lastName = "Jovanović";
        String email = "lana@greenphone.com";
        String address = "Radnička 35, Beograd";

        String url = "http://localhost:" + port + "/api/customer/getCustomerByPhone/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<CustomerViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<CustomerViewDTO>>() {
                });

        RestAPIResponse<CustomerViewDTO> customerViewDTOResponse = responseEntity.getBody();
        assertThat(customerViewDTOResponse).isNotNull();
        assertThat(customerViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(customerViewDTOResponse.isSuccess()).isTrue();
        assertThat(customerViewDTOResponse.getData().getPersonalNumber()).isEqualTo(personalNumber);
        assertThat(customerViewDTOResponse.getData().getFirstName()).isEqualTo(firstName);
        assertThat(customerViewDTOResponse.getData().getLastName()).isEqualTo(lastName);
        assertThat(customerViewDTOResponse.getData().getEmail()).isEqualTo(email);
        assertThat(customerViewDTOResponse.getData().getAddress()).isEqualTo(address);
        assertThat(customerViewDTOResponse.getData().getPhoneNumbers()).contains(phoneNumber);
    }

    @Test
    void testGetAllCustomers(){
        int listSize = 14;

        String url = "http://localhost:" + port + "/api/customer/getAllCustomers";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<List<CustomerViewDTO>>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<CustomerViewDTO>>>() {
                });

        RestAPIResponse<List<CustomerViewDTO>> customerViewDTOListResponse = responseEntity.getBody();
        assertThat(customerViewDTOListResponse).isNotNull();
        assertThat(customerViewDTOListResponse.isSuccess()).isTrue();
        assertThat(customerViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CustomerAddRemovePhoneIT {

        @Test
        @Order(1)
        @Sql(scripts = {"/create_phone.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void testAddPhoneToCustomerIT(){
            testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
            Long id = 1L;
            String phoneNumber = "0788995677";
            String message = "The phone is added to customer.";

            String url = "http://localhost:" + port + "/api/customer/addPhoneToCustomer/" + id;

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

            HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("phoneNumber", phoneNumber);

            ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PATCH, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
            });

            RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
            assertThat(mapResponse).isNotNull();
            assertThat(mapResponse.isSuccess()).isTrue();
            assertThat(mapResponse.getData().get("message")).isEqualTo(message);
        }

        @Test
        @Order(2)
        @Sql(scripts = {"/delete_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void testRemovePhoneFromCustomerIT(){
            testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
            Long id = 1L;
            String phoneNumber = "0788995677";
            String message = "The phone is removed from customer.";

            String url = "http://localhost:" + port + "/api/customer/removePhoneFromCustomer/" + id;

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

            HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("phoneNumber", phoneNumber);

            ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PATCH, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
            });

            RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
            assertThat(mapResponse).isNotNull();
            assertThat(mapResponse.isSuccess()).isTrue();
            assertThat(mapResponse.getData().get("message")).isEqualTo(message);
        }
    }

}
