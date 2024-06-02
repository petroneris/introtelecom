package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.response.RestAPIResponse;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccessControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    void testLogin_validInput_getAccesToken_IT(){
        String username = "dana2";
        String password = "dana2";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);

        String url = "http://localhost:" + port + "/access/login";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
                });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isTrue();
        assertThat(mapResponse.getResponseDate()).isNotNull();
        assertThat(mapResponse.getData().get("access_token")).isNotNull();
    }

    @Test
    void testLogin_invalidInput_errorMessage_IT(){
        String username = "dana2";
        String password = "pswd";
        String error_message = "ERROR OCCURED!";
        String description = "Bad credentials";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);

        String url = "http://localhost:" + port + "/access/login";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<RestAPIResponse<Map<String, String>>> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<RestAPIResponse<Map<String, String>>>() {
                });

        RestAPIResponse<Map<String, String>> mapResponse = responseEntity.getBody();
        assertThat(mapResponse).isNotNull();
        assertThat(mapResponse.isSuccess()).isFalse();
        assertThat(mapResponse.getResponseDate()).isNotNull();
        assertThat(mapResponse.getData().get("error_message")).isEqualTo(error_message);
        assertThat(mapResponse.getData().get("description")).isEqualTo(description);
    }

}
