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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerIntegrationTest {

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
        String username = "dana2"; // CUSTOMER
        String password = "dana2";
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        access_token = jwTtokenGenerator.generateAccess_Token(authentication);
    }

    @Test
    @Sql(scripts = {"/create_currentInfo_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/delete_currentInfo_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testGetCurrentInfoIT() {
        String phoneNumber = "0769317426";
        String packageCode = "12";
        String currCls = "400 min left";
        String currSms = "400 msg left";
        String currInt = "10000.00 MB left";
        String currAsm = "3700.00 MB left";
        String currIcl = "200.00 cu left";
        String currRmg = "200.00 cu left";

        String url = "http://localhost:" + port + "/api/client/getCurrentInfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<RestAPIResponse<ClientCurrentInfo1234ViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<ClientCurrentInfo1234ViewDTO>>() {
                });

        RestAPIResponse<ClientCurrentInfo1234ViewDTO> clientCurrentInfo1234ViewDTOResponse = responseEntity.getBody();
        assertThat(clientCurrentInfo1234ViewDTOResponse).isNotNull();
        assertThat(clientCurrentInfo1234ViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(clientCurrentInfo1234ViewDTOResponse.isSuccess()).isTrue();
        assertThat(clientCurrentInfo1234ViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(clientCurrentInfo1234ViewDTOResponse.getData().getPackageCode()).isEqualTo(packageCode);
        assertThat(clientCurrentInfo1234ViewDTOResponse.getData().getCurrCls()).isEqualTo(currCls);
        assertThat(clientCurrentInfo1234ViewDTOResponse.getData().getCurrSms()).isEqualTo(currSms);
        assertThat(clientCurrentInfo1234ViewDTOResponse.getData().getCurrInt()).isEqualTo(currInt);
        assertThat(clientCurrentInfo1234ViewDTOResponse.getData().getCurrAsm()).isEqualTo(currAsm);
        assertThat(clientCurrentInfo1234ViewDTOResponse.getData().getCurrIcl()).isEqualTo(currIcl);
        assertThat(clientCurrentInfo1234ViewDTOResponse.getData().getCurrRmg()).isEqualTo(currRmg);
    }

    @Test
    void testGetMonthlyBillFactsByYearMonthIT() {
        String phoneNumber = "0769317426";
        String packageCode = "12";
        String firstName = "Danica";
        String lastName= "Ilić";
        String username = "dana2";
        String packageName = "postpaid2";
        int month = 12;
        int year = 2023;
        String monthStr = "DECEMBER";
        String monthlybillTotalprice = "1000.00 cu";
        LocalDateTime monthlybillDateTime = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/client/getMonthlyBillFactsByYearAndMonth";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("year", year)
                .queryParam("month", month);

        ResponseEntity<RestAPIResponse<ClientMonthlyBillFactsPstViewDTO>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<ClientMonthlyBillFactsPstViewDTO>>() {
                });

        RestAPIResponse<ClientMonthlyBillFactsPstViewDTO> clientMonthlyBillFactsPstViewDTOResponse = responseEntity.getBody();
        assertThat(clientMonthlyBillFactsPstViewDTOResponse).isNotNull();
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.isSuccess()).isTrue();
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getData().getPackageCode()).isEqualTo(packageCode);
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getData().getFirstName()).isEqualTo(firstName);
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getData().getLastName()).isEqualTo(lastName);
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getData().getUsername()).isEqualTo(username);
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getData().getPackageName()).isEqualTo(packageName);
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getData().getMonth()).isEqualTo(monthStr);
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getData().getYear()).isEqualTo(year);
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getData().getMonthlybillTotalprice()).isEqualTo(monthlybillTotalprice);
        assertThat(clientMonthlyBillFactsPstViewDTOResponse.getData().getMonthlybillDateTime()).isEqualTo(monthlybillDateTime);
    }

    @Test
    void testGetMonthlyBillFactsStartDateToEndDateIT() {
        int listSize = 3;
        int startMonth = 12;
        int startYear = 2023;
        int endMonth = 2;
        int endYear = 2024;

        String url = "http://localhost:" + port + "/api/client/getMonthlyBillFactsFromStartDateToEndDate";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("startYear", startYear)
                .queryParam("startMonth", startMonth)
                .queryParam("endYear", endYear)
                .queryParam("endMonth", endMonth);

        ResponseEntity<RestAPIResponse<List<ClientMonthlyBillFactsPstViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<ClientMonthlyBillFactsPstViewDTO>>>() {
                });

        RestAPIResponse<List<ClientMonthlyBillFactsPstViewDTO>> clientMonthlyBillFactsPstViewDTOListResponse = responseEntity.getBody();
        assertThat(clientMonthlyBillFactsPstViewDTOListResponse).isNotNull();
        assertThat(clientMonthlyBillFactsPstViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(clientMonthlyBillFactsPstViewDTOListResponse.isSuccess()).isTrue();
        assertThat(clientMonthlyBillFactsPstViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    @Sql(scripts = {"/update_client_password.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePasswordIT() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
        String oldPassword = "dana2";
        String rawNewPassword = "newWorld";
        String checkNewPassword = "newWorld";
        ClientChangePasswordDTO clientChangePasswordDTO = new ClientChangePasswordDTO();
        clientChangePasswordDTO.setOldPassword(oldPassword);
        clientChangePasswordDTO.setNewPassword(rawNewPassword);
        clientChangePasswordDTO.setCheckNewPassword(checkNewPassword);
        String message = "The password is changed.";

        String url = "http://localhost:" + port + "/api/client/changePassword";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<ClientChangePasswordDTO> httpEntity = new HttpEntity<>(clientChangePasswordDTO, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.PATCH, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
        });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    void testGetAllPackagesIT() {
        int listSize = 6;

        String url = "http://localhost:" + port + "/api/client/allPackagePlans_Info";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<List<PackagePlanDTO>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<RestAPIResponse<List<PackagePlanDTO>>>() {
        });

        RestAPIResponse<List<PackagePlanDTO>> packagePlanListDTOResponse = responseEntity.getBody();
        assertThat(packagePlanListDTOResponse).isNotNull();
        assertThat(packagePlanListDTOResponse.isSuccess()).isTrue();
        assertThat(packagePlanListDTOResponse.getData().size()).isEqualTo(listSize);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testGetAllAddOnsIT() {
        int listSize = 6;

        String url = "http://localhost:" + port + "/api/client/allAddOns_Info";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<List<AddOnDTO>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<RestAPIResponse<List<AddOnDTO>>>() {
        });

        RestAPIResponse<List<AddOnDTO>> addOnDTOListResponse = responseEntity.getBody();
        assertThat(addOnDTOListResponse).isNotNull();
        assertThat(addOnDTOListResponse.isSuccess()).isTrue();
        assertThat(addOnDTOListResponse.getData().size()).isEqualTo(listSize);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

}
