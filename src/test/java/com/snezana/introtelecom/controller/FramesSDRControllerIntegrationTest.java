package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.repository.AddonFrameRepo;
import com.snezana.introtelecom.repository.PackageFrameRepo;
import com.snezana.introtelecom.repository.ServiceDetailRecordRepo;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FramesSDRControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    JWTtokenGenerator jwTtokenGenerator;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PackageFrameRepo packageFrameRepo;

    @Autowired
    private AddonFrameRepo addonFrameRepo;

    @Autowired
    private ServiceDetailRecordRepo serviceDetailRecordRepo;

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
    void testGetPackageFrameByIdIT(){
        Long id = 1L;
        String phoneNumber = "0747634418";
        String packageCode = "01";
        String packfrCls = "200 min";
        String packfrSms = "200 msg";
        LocalDateTime packfrStartDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime packfrEndDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/packageFrame/getPackageFrameById/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<PackageFrameViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<PackageFrameViewDTO>>() {
                });

        RestAPIResponse<PackageFrameViewDTO> packageFrameViewDTOResponse = responseEntity.getBody();
        assertThat(packageFrameViewDTOResponse).isNotNull();
        assertThat(packageFrameViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(packageFrameViewDTOResponse.isSuccess()).isTrue();
        assertThat(packageFrameViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(packageFrameViewDTOResponse.getData().getPackageCode()).isEqualTo(packageCode);
        assertThat(packageFrameViewDTOResponse.getData().getPackfrCls()).isEqualTo(packfrCls);
        assertThat(packageFrameViewDTOResponse.getData().getPackfrSms()).isEqualTo(packfrSms);
        assertThat(packageFrameViewDTOResponse.getData().getPackfrStartDateTime()).isEqualTo(packfrStartDateTime);
        assertThat(packageFrameViewDTOResponse.getData().getPackfrEndDateTime()).isEqualTo(packfrEndDateTime);
    }

    @Test
    void testGetPackageFramesByPhoneNumberStartTimeEndTimeIT(){
        int listSize = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/packageFrame/getPackageFramesByPhoneNumberStartTimeEndTime/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("packfrStartDateTime", startDateTime)
                .queryParam("packfrEndDateTime", endDateTime);

        ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<PackageFrameViewDTO>>>() {
                });

        RestAPIResponse<List<PackageFrameViewDTO>> packageFrameViewDTOListResponse = responseEntity.getBody();
        assertThat(packageFrameViewDTOListResponse).isNotNull();
        assertThat(packageFrameViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(packageFrameViewDTOListResponse.isSuccess()).isTrue();
        assertThat(packageFrameViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    void testGetPackageFramesByPhoneNumberStartTimeIT(){
        int listSize = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/packageFrame/getPackageFramesByPhoneNumberStartTime/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("packfrStartDateTime", startDateTime);

        ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<PackageFrameViewDTO>>>() {
                });

        RestAPIResponse<List<PackageFrameViewDTO>> packageFrameViewDTOListResponse = responseEntity.getBody();
        assertThat(packageFrameViewDTOListResponse).isNotNull();
        assertThat(packageFrameViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(packageFrameViewDTOListResponse.isSuccess()).isTrue();
        assertThat(packageFrameViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    void testGetPackageFramesByPackageCodeStartTimeEndTimeIT(){
        int listSize = 2;
        String packageCode = "01";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/packageFrame/getPackageFramesByPackageCodeStartTimeEndTime/" + packageCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("packfrStartDateTime", startDateTime)
                .queryParam("packfrEndDateTime", endDateTime);

        ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<PackageFrameViewDTO>>>() {
                });

        RestAPIResponse<List<PackageFrameViewDTO>> packageFrameViewDTOListResponse = responseEntity.getBody();
        assertThat(packageFrameViewDTOListResponse).isNotNull();
        assertThat(packageFrameViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(packageFrameViewDTOListResponse.isSuccess()).isTrue();
        assertThat(packageFrameViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    void testGetPackageFramesByPackageCodeStartTimeIT(){
        int listSize = 2;
        String packageCode = "01";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/packageFrame/getPackageFramesByPackageCodeStartTime/" + packageCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("packfrStartDateTime", startDateTime);

        ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<PackageFrameViewDTO>>>() {
                });

        RestAPIResponse<List<PackageFrameViewDTO>> packageFrameViewDTOListResponse = responseEntity.getBody();
        assertThat(packageFrameViewDTOListResponse).isNotNull();
        assertThat(packageFrameViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(packageFrameViewDTOListResponse.isSuccess()).isTrue();
        assertThat(packageFrameViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    @Sql(scripts = {"/update_package_frame.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePackageFrameStatusIT(){
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
        Long id = 3L;
        String message = "The package frame status is changed.";

        String url = "http://localhost:" + port + "/api/packageFrame/changePackageFrameStatus/" + id;

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
    @Sql(scripts = {"/create_package_frame.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeletePackageFrameIT(){
        Long packfrId = packageFrameRepo.maxPackfr_id();
        String message = "The package frame '" +packfrId + "' is deleted!";

        String url = "http://localhost:" + port + "/api/packageFrame/deletePackageFrame/" + packfrId;

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

    @Test
    @Sql(scripts = {"/delete_addon_frame.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveAddonFrameIT(){
        String addonCode = "ADDCLS";
        String phoneNumber = "0747634418";
        AddonFrameSaveDTO addonFrameSaveDTO = new AddonFrameSaveDTO();
        addonFrameSaveDTO.setAddonCode(addonCode);
        addonFrameSaveDTO.setPhoneNumber(phoneNumber);
        String message = "The new addon frame is saved.";

        String url = "http://localhost:" + port + "/api/addon/saveNewAddonFrame";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<AddonFrameSaveDTO> requestEntity = new HttpEntity<>(addonFrameSaveDTO, headers);

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
    void testGetAddonFrameByIdIT(){
        Long id = 1L;
        String phoneNumber = "0747634418";
        String addonCode = "ADDCLS";
        LocalDateTime addfrStartDateTime = LocalDateTime.of(2023, 1, 22, 0, 0, 0, 0);
        LocalDateTime addfrEndDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/addonFrame/getAddonFrameById/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<AddonFrameViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<AddonFrameViewDTO>>() {
                });

        RestAPIResponse<AddonFrameViewDTO> addonFrameViewDTOResponse = responseEntity.getBody();
        assertThat(addonFrameViewDTOResponse).isNotNull();
        assertThat(addonFrameViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(addonFrameViewDTOResponse.isSuccess()).isTrue();
        assertThat(addonFrameViewDTOResponse.getData().getAddfrId()).isEqualTo(id);
        assertThat(addonFrameViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(addonFrameViewDTOResponse.getData().getAddonCode()).isEqualTo(addonCode);
        assertThat(addonFrameViewDTOResponse.getData().getAddfrStartDateTime()).isEqualTo(addfrStartDateTime);
        assertThat(addonFrameViewDTOResponse.getData().getAddfrEndDateTime()).isEqualTo(addfrEndDateTime);
    }

    @Test
    void testGetAddonFramesByPhoneNumberStartTimeEndTimeIT(){
        int listSize = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/addonFrame/getAddonFramesByPhoneNumberStartTimeEndTime/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("addfrStartDateTime", startDateTime)
                .queryParam("addfrEndDateTime", endDateTime);

        ResponseEntity<RestAPIResponse<List<AddonFrameViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<AddonFrameViewDTO>>>() {
                });

        RestAPIResponse<List<AddonFrameViewDTO>> addonFrameViewDTOListResponse = responseEntity.getBody();
        assertThat(addonFrameViewDTOListResponse).isNotNull();
        assertThat(addonFrameViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(addonFrameViewDTOListResponse.isSuccess()).isTrue();
        assertThat(addonFrameViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    void testGetAddonFramesByPhoneNumberStartTimeIT(){
        int listSize = 2;
        String phoneNumber = "0747634418";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/addonFrame/getAddonFramesByPhoneNumberStartTime/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("addfrStartDateTime", startDateTime);

        ResponseEntity<RestAPIResponse<List<AddonFrameViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<AddonFrameViewDTO>>>() {
                });

        RestAPIResponse<List<AddonFrameViewDTO>> addonFrameViewDTOListResponse = responseEntity.getBody();
        assertThat(addonFrameViewDTOListResponse).isNotNull();
        assertThat(addonFrameViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(addonFrameViewDTOListResponse.isSuccess()).isTrue();
        assertThat(addonFrameViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    void testGetAddonFramesByAddonCodeStartTimeEndTimeIT(){
        int listSize = 2;
        String addonCode = "ADDCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/addonFrame/getAddonFramesByAddonCodeStartTimeEndTime/" + addonCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("addfrStartDateTime", startDateTime)
                .queryParam("addfrEndDateTime", endDateTime);

        ResponseEntity<RestAPIResponse<List<AddonFrameViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<AddonFrameViewDTO>>>() {
                });

        RestAPIResponse<List<AddonFrameViewDTO>> addonFrameViewDTOListResponse = responseEntity.getBody();
        assertThat(addonFrameViewDTOListResponse).isNotNull();
        assertThat(addonFrameViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(addonFrameViewDTOListResponse.isSuccess()).isTrue();
        assertThat(addonFrameViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    void testGetAddonFramesByAddonCodeStartTimeIT(){
        int listSize = 2;
        String addonCode = "ADDCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/addonFrame/getAddonFramesByAddonCodeStartTime/" + addonCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("addfrStartDateTime", startDateTime);

        ResponseEntity<RestAPIResponse<List<AddonFrameViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<AddonFrameViewDTO>>>() {
                });

        RestAPIResponse<List<AddonFrameViewDTO>> addonFrameViewDTOListResponse = responseEntity.getBody();
        assertThat(addonFrameViewDTOListResponse).isNotNull();
        assertThat(addonFrameViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(addonFrameViewDTOListResponse.isSuccess()).isTrue();
        assertThat(addonFrameViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    @Sql(scripts = {"/update_addon_frame.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangeAddonFrameStatusIT(){
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
        Long id = 3L;
        String message = "The addon frame status is changed.";

        String url = "http://localhost:" + port + "/api/addonFrame/changeAddonFrameStatus/" + id;

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
    @Sql(scripts = {"/create_addon_frame.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeleteAddonFrameIT(){
        Long addfrId = addonFrameRepo.maxAddfr_id();
        String message = "The addon frame '" +addfrId + "' is deleted!";

        String url = "http://localhost:" + port + "/api/addonFrame/deleteAddonFrame/" + addfrId;

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

    /*
       it is a test for normal completion of SDR (message =  "The new Service Detail Record is saved.");
   */
    @Test
    @Sql(scripts = {"/delete_sdr.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveServiceDetailRecord_notEOS_IT() {
        String phoneNumber = "0769317426";
        String serviceCode = "SDRINT";
        String calledNumber = "-";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 10, 12, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 10, 13, 10, 0, 0);
        int msgAmount = 0;
        BigDecimal mbAmount = BigDecimal.valueOf(1000.00);
        mbAmount = mbAmount.setScale(2, RoundingMode.UP);
        String message =  "The new Service Detail Record is saved.";

        ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO = new ServiceDetailRecordSaveDTO();
        serviceDetailRecordSaveDTO.setPhoneNumber(phoneNumber);
        serviceDetailRecordSaveDTO.setServiceCode(serviceCode);
        serviceDetailRecordSaveDTO.setCalledNumber(calledNumber);
        serviceDetailRecordSaveDTO.setMsgAmount(msgAmount);
        serviceDetailRecordSaveDTO.setMbAmount(mbAmount);
        serviceDetailRecordSaveDTO.setSdrStartDateTime(startDateTime);
        serviceDetailRecordSaveDTO.setSdrEndDateTime(endDateTime);

        String url = "http://localhost:" + port + "/api/sdr/saveNewServiceDetailRecord";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<ServiceDetailRecordSaveDTO> requestEntity = new HttpEntity<>(serviceDetailRecordSaveDTO, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
                });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    /*
       it is a test where internet service is interrupted by EOS (End of Service);
       there are other five service cases where SDR service is interrupted, but these tests
       demand more test data with complex relation and time dependence among them
   */
    @Test
    @Sql(scripts = {"/delete_sdr.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveServiceDetailRecord_EOS_IT() {
        String phoneNumber = "0769317426";
        String serviceCode = "SDRINT";
        String calledNumber = "-";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2, 20, 12, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 2, 20, 19, 10, 0, 0);
        int msgAmount = 0;
        BigDecimal mbAmount = BigDecimal.valueOf(9000.00);
        mbAmount = mbAmount.setScale(2, RoundingMode.UP);
        String message =   "Internet service is interrupted by EOS";

        ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO = new ServiceDetailRecordSaveDTO();
        serviceDetailRecordSaveDTO.setPhoneNumber(phoneNumber);
        serviceDetailRecordSaveDTO.setServiceCode(serviceCode);
        serviceDetailRecordSaveDTO.setCalledNumber(calledNumber);
        serviceDetailRecordSaveDTO.setMsgAmount(msgAmount);
        serviceDetailRecordSaveDTO.setMbAmount(mbAmount);
        serviceDetailRecordSaveDTO.setSdrStartDateTime(startDateTime);
        serviceDetailRecordSaveDTO.setSdrEndDateTime(endDateTime);

        String url = "http://localhost:" + port + "/api/sdr/saveNewServiceDetailRecord";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<ServiceDetailRecordSaveDTO> requestEntity = new HttpEntity<>(serviceDetailRecordSaveDTO, headers);

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
    void testGetServiceDetailRecordByIdIT(){
        Long sdrId = 1L;
        String phoneNumber = "0769317426";
        String serviceCode = "SDRCLS";
        String calledNumber = "0736336712";
        int msgAmount = 0;

        String url = "http://localhost:" + port + "/api/sdr/getServiceDetailRecordById/" + sdrId;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<ServiceDetailRecordViewDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<ServiceDetailRecordViewDTO>>() {
                });

        RestAPIResponse<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOResponse = responseEntity.getBody();
        assertThat(serviceDetailRecordViewDTOResponse).isNotNull();
        assertThat(serviceDetailRecordViewDTOResponse.getResponseDate()).isNotNull();
        assertThat(serviceDetailRecordViewDTOResponse.isSuccess()).isTrue();
        assertThat(serviceDetailRecordViewDTOResponse.getData().getSdrId()).isEqualTo(sdrId);
        assertThat(serviceDetailRecordViewDTOResponse.getData().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(serviceDetailRecordViewDTOResponse.getData().getServiceCode()).isEqualTo(serviceCode);
        assertThat(serviceDetailRecordViewDTOResponse.getData().getCalledNumber()).isEqualTo(calledNumber);
        assertThat(serviceDetailRecordViewDTOResponse.getData().getMsgAmount()).isEqualTo(msgAmount);
    }

    @Test
    void testGetServiceDetailRecordsByPhoneNumberStartTimeEndTimeIT(){
        int listSize = 8;
        String phoneNumber = "0769317426";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/sdr/getServiceDetailRecordsByPhoneNumberStartTimeEndTime/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("sdrStartDateTime", startDateTime)
                .queryParam("sdrEndDateTime", endDateTime);

        ResponseEntity<RestAPIResponse<List<ServiceDetailRecordViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<ServiceDetailRecordViewDTO>>>() {
                });

        RestAPIResponse<List<ServiceDetailRecordViewDTO>> serviceDetailRecordViewDTOListResponse = responseEntity.getBody();
        assertThat(serviceDetailRecordViewDTOListResponse).isNotNull();
        assertThat(serviceDetailRecordViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(serviceDetailRecordViewDTOListResponse.isSuccess()).isTrue();
        assertThat(serviceDetailRecordViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    void testGetServiceDetailRecordsByPhoneNumberStartTimeIT(){
        int listSize = 8;
        String phoneNumber = "0769317426";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/sdr/getServiceDetailRecordsByPhoneNumberStartTime/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("sdrStartDateTime", startDateTime);

        ResponseEntity<RestAPIResponse<List<ServiceDetailRecordViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<ServiceDetailRecordViewDTO>>>() {
                });

        RestAPIResponse<List<ServiceDetailRecordViewDTO>> serviceDetailRecordViewDTOListResponse = responseEntity.getBody();
        assertThat(serviceDetailRecordViewDTOListResponse).isNotNull();
        assertThat(serviceDetailRecordViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(serviceDetailRecordViewDTOListResponse.isSuccess()).isTrue();
        assertThat(serviceDetailRecordViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    void testGetServiceDetailRecordsByPhoneNumberSdrCodeStartTimeEndTimeIT(){
        int listSize = 2;
        String phoneNumber = "0769317426";
        String serviceCode = "SDRCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/sdr/getServiceDetailRecordsByPhoneNumberSdrCodeStartTimeEndTime/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("sdrCode", serviceCode)
                .queryParam("sdrStartDateTime", startDateTime)
                .queryParam("sdrEndDateTime", endDateTime);

        ResponseEntity<RestAPIResponse<List<ServiceDetailRecordViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<ServiceDetailRecordViewDTO>>>() {
                });

        RestAPIResponse<List<ServiceDetailRecordViewDTO>> serviceDetailRecordViewDTOListResponse = responseEntity.getBody();
        assertThat(serviceDetailRecordViewDTOListResponse).isNotNull();
        assertThat(serviceDetailRecordViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(serviceDetailRecordViewDTOListResponse.isSuccess()).isTrue();
        assertThat(serviceDetailRecordViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    void testGetServiceDetailRecordsByPhoneNumberSdrCodeStartTimeIT(){
        int listSize = 2;
        String phoneNumber = "0769317426";
        String serviceCode = "SDRCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, 2, 1, 0, 0, 0, 0);

        String url = "http://localhost:" + port + "/api/sdr/getServiceDetailRecordsByPhoneNumberSdrCodeStartTime/" + phoneNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("sdrCode", serviceCode)
                .queryParam("sdrStartDateTime", startDateTime);

        ResponseEntity<RestAPIResponse<List<ServiceDetailRecordViewDTO>>> responseEntity = testRestTemplate.exchange(
                uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<List<ServiceDetailRecordViewDTO>>>() {
                });

        RestAPIResponse<List<ServiceDetailRecordViewDTO>> serviceDetailRecordViewDTOListResponse = responseEntity.getBody();
        assertThat(serviceDetailRecordViewDTOListResponse).isNotNull();
        assertThat(serviceDetailRecordViewDTOListResponse.getResponseDate()).isNotNull();
        assertThat(serviceDetailRecordViewDTOListResponse.isSuccess()).isTrue();
        assertThat(serviceDetailRecordViewDTOListResponse.getData().size()).isEqualTo(listSize);
    }

    @Test
    @Sql(scripts = {"/create_sdr.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeleteServiceDetailRecordIT(){
        Long sdrId = serviceDetailRecordRepo.maxSdr_id();
        String message = "The Service Detail Record '" +sdrId + "' is deleted!";

        String url = "http://localhost:" + port + "/api/sdr/deleteServiceDetailRecord/" + sdrId;

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
