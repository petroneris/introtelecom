package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.CurrentInfo1234ViewDTO;
import com.snezana.introtelecom.dto.MonthlyBillFactsViewDTO;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrentInfoMonthlyBillFactsControllerIntegrationTest {

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
        String username = "mika";
        String password = "mika";
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        access_token = jwTtokenGenerator.generateAccess_Token(authentication);
    }

    @Test
    @Sql(scripts = {"/create_currentInfo_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/delete_currentInfo_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testGetCurrentInfoByPhoneIT() {
        String phoneNumber = "0769317426";
        String packageCode = "12";
        String currCls = "400 min left";
        String currSms = "400 msg left";
        String currInt = "10000.00 MB left";
        String currAsm = "3700.00 MB left";
        String currIcl = "200.00 cu left";
        String currRmg = "200.00 cu left";

        String url = "http://localhost:" + port + "/api/currentInfo/getCurrentInfoByPhoneNumber/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<RestAPIResponse<CurrentInfo1234ViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<CurrentInfo1234ViewDTO>>() {
                });

        RestAPIResponse<CurrentInfo1234ViewDTO> currentInfo1234ViewDTOResponse = responseEntity.getBody();
        assertThat(currentInfo1234ViewDTOResponse).isNotNull();
        assertThat(currentInfo1234ViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(currentInfo1234ViewDTOResponse.isSuccess()).isTrue();
        assertThat(currentInfo1234ViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(currentInfo1234ViewDTOResponse.getData().getPackageCode()).isEqualTo(packageCode);
        assertThat(currentInfo1234ViewDTOResponse.getData().getCurrCls()).isEqualTo(currCls);
        assertThat(currentInfo1234ViewDTOResponse.getData().getCurrSms()).isEqualTo(currSms);
        assertThat(currentInfo1234ViewDTOResponse.getData().getCurrInt()).isEqualTo(currInt);
        assertThat(currentInfo1234ViewDTOResponse.getData().getCurrAsm()).isEqualTo(currAsm);
        assertThat(currentInfo1234ViewDTOResponse.getData().getCurrIcl()).isEqualTo(currIcl);
        assertThat(currentInfo1234ViewDTOResponse.getData().getCurrRmg()).isEqualTo(currRmg);
    }

    @Test
    void testGetMonthlyBillFactsByIdIT() {
        Long monthlybillId = 1L;
        String phoneNumber = "0769317426";
        String packageCode = "12";
        String month = "DECEMBER";
        int year = 2023;
        String monthlybillTotalprice = "1000.00 cu";
        LocalDateTime monthlybillDateTime = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/monthlyBillFacts/getMonthlyBillFactsById/" + monthlybillId;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<RestAPIResponse<MonthlyBillFactsViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<MonthlyBillFactsViewDTO>>() {
                });

        RestAPIResponse<MonthlyBillFactsViewDTO> monthlyBillFactsViewDTOResponse = responseEntity.getBody();
        assertThat(monthlyBillFactsViewDTOResponse).isNotNull();
        assertThat(monthlyBillFactsViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(monthlyBillFactsViewDTOResponse.isSuccess()).isTrue();
        assertThat(monthlyBillFactsViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getPackageCode()).isEqualTo(packageCode);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getMonth()).isEqualTo(month);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getYear()).isEqualTo(year);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getMonthlybillTotalprice()).isEqualTo(monthlybillTotalprice);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getMonthlybillDateTime()).isEqualTo(monthlybillDateTime);
    }

    @Test
    void testGetMonthlyBillFactsByPhoneAndYearMonthIT() {
        Long monthlybillId = 1L;
        String phoneNumber = "0769317426";
        String packageCode = "12";
        int month = 12;
        int year = 2023;
        String monthStr = "DECEMBER";
        String monthlybillTotalprice = "1000.00 cu";
        LocalDateTime monthlybillDateTime = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/monthlyBillFacts/getMonthlyBillFactsByPhoneNumberYearAndMonth/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("year", year)
                .queryParam("month", month);

        ResponseEntity<RestAPIResponse<MonthlyBillFactsViewDTO>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<MonthlyBillFactsViewDTO>>() {
                });

        RestAPIResponse<MonthlyBillFactsViewDTO> monthlyBillFactsViewDTOResponse = responseEntity.getBody();
        assertThat(monthlyBillFactsViewDTOResponse).isNotNull();
        assertThat(monthlyBillFactsViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(monthlyBillFactsViewDTOResponse.isSuccess()).isTrue();
        assertThat(monthlyBillFactsViewDTOResponse.getData().getMonthlybillId()).isEqualTo(monthlybillId);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getPackageCode()).isEqualTo(packageCode);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getMonth()).isEqualTo(monthStr);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getYear()).isEqualTo(year);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getMonthlybillTotalprice()).isEqualTo(monthlybillTotalprice);
        assertThat(monthlyBillFactsViewDTOResponse.getData().getMonthlybillDateTime()).isEqualTo(monthlybillDateTime);
    }

    @Test
    void testGetMonthlyBillFactsByPhoneFromStartDateToEndDateIT() {
        int listSize = 3;
        String phoneNumber = "0769317426";
        int startMonth = 12;
        int startYear = 2023;
        int endMonth = 2;
        int endYear = 2024;

        String url = "http://localhost:" + port + "/api/monthlyBillFacts/getMonthlyBillFactsByPhoneFromStartDateToEndDate/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("startYear", startYear)
                .queryParam("startMonth", startMonth)
                .queryParam("endYear", endYear)
                .queryParam("endMonth", endMonth);

        ResponseEntity<RestAPIResponse<List<MonthlyBillFactsViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<MonthlyBillFactsViewDTO>>>() {
                });

        RestAPIResponse<List<MonthlyBillFactsViewDTO>> monthlyBillFactsViewDTOListResponse = responseEntity.getBody();
        assertThat(monthlyBillFactsViewDTOListResponse).isNotNull();
        assertThat(monthlyBillFactsViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(monthlyBillFactsViewDTOListResponse.isSuccess()).isTrue();
        assertThat(monthlyBillFactsViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

}

