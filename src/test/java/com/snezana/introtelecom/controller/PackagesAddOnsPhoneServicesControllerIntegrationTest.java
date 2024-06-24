package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.dto.PhoneServiceDTO;
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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PackagesAddOnsPhoneServicesControllerIntegrationTest {

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
    void testGetPackageByPackageCodeIT() {
        String packageName = "prepaid1";
        String packageCode = "01";

        String url = "http://localhost:" + port + "/api/package/getPackagePlanByPackageCode/" + packageCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<PackagePlanDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<PackagePlanDTO>>() {
                });

        RestAPIResponse<PackagePlanDTO> packagePlanDTOResponse = responseEntity.getBody();
        assertThat(packagePlanDTOResponse).isNotNull();
        assertThat(packagePlanDTOResponse.getResponseDate()).isNotNull();
        assertThat(packagePlanDTOResponse.isSuccess()).isTrue();
        assertThat(packagePlanDTOResponse.getData().getPackageCode()).isEqualTo(packageCode);
        assertThat(packagePlanDTOResponse.getData().getPackageName()).isEqualTo(packageName);
    }

    @Test
    void testGetAllPackagesIT() {
        int listSize = 7;

        String url = "http://localhost:" + port + "/api/package/getAllPackagePlans";

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
    @Sql(scripts = {"/update_package_plan.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangePackagePriceIT(){
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
        String packageCode = "01";
        String message = "The package price of package code= " + packageCode + " is changed.";
        BigDecimal packagePrice = BigDecimal.valueOf(350.00);
        packagePrice = packagePrice.setScale( 2, RoundingMode.UP);

        String url = "http://localhost:" + port + "/api/package/changePackagePrice/" + packageCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("packagePrice", packagePrice);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PATCH, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {});

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    void testGetAddOnByAddOnCodeIT() {
        String addonCode = "ADDCLS";

        String url = "http://localhost:" + port + "/api/addon/getAddOnByAddOnCode/" + addonCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<AddOnDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<AddOnDTO>>() {
                });

        RestAPIResponse<AddOnDTO> addOnDTOResponse = responseEntity.getBody();
        assertThat(addOnDTOResponse).isNotNull();
        assertThat(addOnDTOResponse.getResponseDate()).isNotNull();
        assertThat(addOnDTOResponse.isSuccess()).isTrue();
        assertThat(addOnDTOResponse.getData().getAddonCode()).isEqualTo(addonCode);
    }

    @Test
    void testGetAllAddOnsIT() {
        int listSize = 6;

        String url = "http://localhost:" + port + "/api/addon/getAllAddOns";

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

    @Test
    @Sql(scripts = {"/update_addon.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangeAddOnPriceIT(){
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
        String addonCode = "ADDCLS";
        String message = "The addon price of addon code= " +addonCode + " is changed.";
        BigDecimal addonPrice = BigDecimal.valueOf(150.00);
        addonPrice = addonPrice.setScale( 2, RoundingMode.UP);

        String url = "http://localhost:" + port + "/api/addon/changeAddOnPrice/" + addonCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("addonPrice", addonPrice);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PATCH, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {});

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

    @Test
    void testGetPhoneServiceByServiceCodeIT() {
        String serviceCode = "SDRCLS";

        String url = "http://localhost:" + port + "/api/service/getPhoneServiceByServiceCode/" + serviceCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> requestEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<PhoneServiceDTO>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<PhoneServiceDTO>>() {
                });

        RestAPIResponse<PhoneServiceDTO> phoneServiceDTOResponse = responseEntity.getBody();
        assertThat(phoneServiceDTOResponse).isNotNull();
        assertThat(phoneServiceDTOResponse.getResponseDate()).isNotNull();
        assertThat(phoneServiceDTOResponse.isSuccess()).isTrue();
        assertThat(phoneServiceDTOResponse.getData().getServiceCode()).isEqualTo(serviceCode);
    }

    @Test
    void testGetAllPhoneServicesIT() {
        int listSize = 13;

        String url = "http://localhost:" + port + "/api/service/getAllPhoneServices";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        ResponseEntity<RestAPIResponse<List<PhoneServiceDTO>>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<RestAPIResponse<List<PhoneServiceDTO>>>() {
        });

        RestAPIResponse<List<PhoneServiceDTO>> phoneServiceDTOListResponse = responseEntity.getBody();
        assertThat(phoneServiceDTOListResponse).isNotNull();
        assertThat(phoneServiceDTOListResponse.isSuccess()).isTrue();
        assertThat(phoneServiceDTOListResponse.getData().size()).isEqualTo(listSize);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Sql(scripts = {"/update_service.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testChangeServicePriceIT(){
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // for PATCH request
        String serviceCode = "SDRRMGSZ1";
        String message = "The service price of service code= " +serviceCode + " is changed.";
        BigDecimal servicePrice = BigDecimal.valueOf(3.00);
        servicePrice = servicePrice.setScale( 2, RoundingMode.UP);

        String url = "http://localhost:" + port + "/api/service/changeServicePrice/" + serviceCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        HttpEntity<String> httpEntity = new HttpEntity<>( null, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("servicePrice", servicePrice);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PATCH, httpEntity, new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {});

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getData().get("message")).isEqualTo(message);
    }

}

