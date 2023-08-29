package com.snezana.introtelecom.controllers;

import com.snezana.introtelecom.dto.UserLoginDTO;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/access")
public class AccessController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(tags = "Access Controller", summary = "User Authentication", description = "Authenticate the user and return a JWT token if the user is valid.")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> login(@RequestBody(description = "Created user object", required = true,
            content = @Content(
                    schema = @Schema(implementation = UserLoginDTO.class))) UserLoginDTO userDto){
        Map<String, String> token = authenticationService.login(userDto);
        return ResponseEntity.ok(RestAPIResponse.of(token));
    }

}
