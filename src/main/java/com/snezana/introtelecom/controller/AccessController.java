package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.UserLoginDTO;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/access")
@RequiredArgsConstructor
public class AccessController {

    private final AuthenticationService authenticationService;

    @Operation(tags = "Access Controller", summary = "User authentication", description = "Authenticates the user and returns a JWT token if the user is valid.")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> login(@RequestBody(description = "Create user object", required = true,
            content = @Content(
                    schema = @Schema(implementation = UserLoginDTO.class))) UserLoginDTO userDto){
        Map<String, String> token = authenticationService.login(userDto);
        return ResponseEntity.ok(RestAPIResponse.of(token));
    }

}
