package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.security.JWTtokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class UserPhoneControllerIntegrationTest {

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
    @Sql(scripts = {"/delete_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSavePhoneIT(){
        String phoneNumber = "0788995677";
        String checkPhoneNumber = "0788995677";
        String packageCode = "11";

        PhoneSaveDTO phoneSaveDTO = new PhoneSaveDTO();
        phoneSaveDTO.setPhoneNumber(phoneNumber);
        phoneSaveDTO.setCheckPhoneNumber(checkPhoneNumber);
        phoneSaveDTO.setPackageCode(packageCode);
        String message = "The phone '" + phoneSaveDTO.getPhoneNumber() + "' is saved.";

        String url = "http://localhost:" + port + "/api/phone/saveNewPhone";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<PhoneSaveDTO> requestEntity = new HttpEntity<>( phoneSaveDTO, headers);

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
    @Sql(scripts = {"/update_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePackageCodeIT(){
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
        String phoneNumber = "0742347426";
        String packageCode = "14";
        String message = "The package code is changed.";

        String url = "http://localhost:" + port + "/api/phone/changePackageCode/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("packageCode", packageCode);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PATCH, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {});

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    @Sql(scripts = {"/update_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePhoneStatusIT() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
        String phoneNumber = "0742347426";
        String message = "The phone status is changed.";

        String url = "http://localhost:" + port + "/api/phone/changePhoneStatus/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.PATCH, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
        });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        System.out.println(mapResponse);
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    void testGetPhoneIT() {
        String phoneNumber = "0742347426";
        String packageCode = "13";

        String url = "http://localhost:" + port + "/api/phone/getPhone/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<PhoneViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<PhoneViewDTO>>() {
                });

        RestAPIResponse<PhoneViewDTO> phoneViewDTOResponse = responseEntity.getBody();
        assertThat(phoneViewDTOResponse).isNotNull();
        assertThat(phoneViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(phoneViewDTOResponse.isSuccess()).isTrue();
        assertThat(phoneViewDTOResponse.getData().getPackageCode()).isEqualTo(packageCode);
        assertThat(phoneViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetPhonesByPackageCodeIT() {
        int listSize = 4;
        String packageCode = "13";

        String url = "http://localhost:" + port + "/api/phone/getPhonesByPackageCode/" + packageCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<List<PhoneViewDTO>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<RestAPIResponse<List<PhoneViewDTO>>>() {
        });

        RestAPIResponse<List<PhoneViewDTO>> phoneViewDTOListResponse = responseEntity.getBody();
        assertThat(phoneViewDTOListResponse).isNotNull();
        assertThat(phoneViewDTOListResponse.isSuccess()).isTrue();
        assertThat(phoneViewDTOListResponse.getData().size()).isEqualTo(listSize);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testGetAllAdminPhonesIT() {
        int listSize = 2;

        String url = "http://localhost:" + port + "/api/phone/getAllAdminPhones/";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<List<PhoneViewDTO>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<RestAPIResponse<List<PhoneViewDTO>>>() {
        });

        RestAPIResponse<List<PhoneViewDTO>> phoneViewDTOListResponse = responseEntity.getBody();
        assertThat(phoneViewDTOListResponse).isNotNull();
        assertThat(phoneViewDTOListResponse.isSuccess()).isTrue();
        assertThat(phoneViewDTOListResponse.getData().size()).isEqualTo(listSize);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testGetAllCustomersPhonesIT() {
        int listSize = 24;

        String url = "http://localhost:" + port + "/api/phone/getAllCustomersPhones/";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<List<PhoneViewDTO>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<RestAPIResponse<List<PhoneViewDTO>>>() {
        });

        RestAPIResponse<List<PhoneViewDTO>> phoneViewDTOListResponse = responseEntity.getBody();
        assertThat(phoneViewDTOListResponse).isNotNull();
        assertThat(phoneViewDTOListResponse.isSuccess()).isTrue();
        assertThat(phoneViewDTOListResponse.getData().size()).isEqualTo(listSize);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Sql(scripts = {"/create_phone.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/delete_user.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = {"/delete_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveUserIT(){
        String phoneNumber = "0788995677";
        String username = "sneza5";
        String password = "newWorld";
        String checkPassword = "newWorld";
        String roleType = "CUSTOMER";
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        userSaveDTO.setPhoneNumber(phoneNumber);
        userSaveDTO.setUsername(username);
        userSaveDTO.setPassword(password);
        userSaveDTO.setCheckPassword(checkPassword);
        userSaveDTO.setRoleType(roleType);
        String message = "The user '" + userSaveDTO.getUsername() + "' is saved.";

        String url = "http://localhost:" + port + "/api/user/saveNewUser";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<UserSaveDTO> requestEntity = new HttpEntity<>( userSaveDTO, headers);

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
    void testGetUserByPhoneNumberIT() {
        String phoneNumber = "0758519203";
        String username = "sava3";

        String url = "http://localhost:" + port + "/api/user/getUserByPhoneNumber/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<UserViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<UserViewDTO>>() {
                });

        RestAPIResponse<UserViewDTO> userViewDTOResponse = responseEntity.getBody();
        assertThat(userViewDTOResponse).isNotNull();
        assertThat(userViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(userViewDTOResponse.isSuccess()).isTrue();
        assertThat(userViewDTOResponse.getData().getUsername()).isEqualTo(username);
        assertThat(userViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetUserByUsernameIT() {
        String username = "sava3";
        String phoneNumber = "0758519203";

        String url = "http://localhost:" + port + "/api/user/getUserByUsername/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<UserViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<UserViewDTO>>() {
                });

        RestAPIResponse<UserViewDTO> userViewDTOResponse = responseEntity.getBody();
        assertThat(userViewDTOResponse).isNotNull();
        assertThat(userViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(userViewDTOResponse.isSuccess()).isTrue();
        assertThat(userViewDTOResponse.getData().getUsername()).isEqualTo(username);
        assertThat(userViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testGetAllAdminUsersIT() {
        int listSize = 2;

        String url = "http://localhost:" + port + "/api/user/getAllAdminUsers";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<List<UserViewDTO>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<RestAPIResponse<List<UserViewDTO>>>() {
        });

        RestAPIResponse<List<UserViewDTO>> userViewDTOListResponse = responseEntity.getBody();
        assertThat(userViewDTOListResponse).isNotNull();
        assertThat(userViewDTOListResponse.isSuccess()).isTrue();
        assertThat(userViewDTOListResponse.getData().size()).isEqualTo(listSize);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testGetAllCustomersUsersIT() {
        int listSize = 24;

        String url = "http://localhost:" + port + "/api/user/getAllCustomersUsers";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<List<UserViewDTO>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<RestAPIResponse<List<UserViewDTO>>>() {
        });

        RestAPIResponse<List<UserViewDTO>> userViewDTOListResponse = responseEntity.getBody();
        assertThat(userViewDTOListResponse).isNotNull();
        assertThat(userViewDTOListResponse.isSuccess()).isTrue();
        assertThat(userViewDTOListResponse.getData().size()).isEqualTo(listSize);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Sql(scripts = {"/update_user_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangeUserStatusIT() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
        String phoneNumber = "0758519203";
        String message = "The user status is changed.";

        String url = "http://localhost:" + port + "/api/user/changeUserStatus/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.PATCH, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
        });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    @Sql(scripts = {"/update_user_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangeUserPasswordIT() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
        String username = "sava3";
        String oldPassword = "sava3";
        String rawNewPassword = "newWorld";
        String checkNewPassword = "newWorld";
        UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO();
        userChangePasswordDTO.setUsername(username);
        userChangePasswordDTO.setOldPassword(oldPassword);
        userChangePasswordDTO.setNewPassword(rawNewPassword);
        userChangePasswordDTO.setCheckNewPassword(checkNewPassword);
        String message = "The user password is changed.";

        String url = "http://localhost:" + port + "/api/user/changeUserPassword";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<UserChangePasswordDTO> httpEntity = new HttpEntity<>(userChangePasswordDTO, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.PATCH, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
        });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    @Sql(scripts = {"/create_phone.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/create_user_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/delete_phone.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testDeleteUserIT(){
        String username = "sneza5";
        String message = "The user '" +username + "' is deleted!";

        String url = "http://localhost:" + port + "/api/user/deleteUser/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
        });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

}
