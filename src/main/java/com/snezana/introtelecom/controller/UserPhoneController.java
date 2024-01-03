package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.response.RestMessage;
import com.snezana.introtelecom.service.UserPhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@RequiredArgsConstructor
public class UserPhoneController {

//    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserPhoneController.class);

    private final UserPhoneService usersPhonesService;

    @Operation(tags = "UserPhone Controller", description = "Save a new phone")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/phone/saveNewPhone", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> savePhone(@RequestBody @Valid PhoneSaveDTO phoneSaveDto){
        usersPhonesService.saveNewPhone(phoneSaveDto);
        String message = "The phone '" + phoneSaveDto.getPhoneNumber() + "' is saved.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "UserPhone Controller", description = "Update a package code")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "/phone/changePackageCode/{phoneNumber}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changePackageCode(@PathVariable String phoneNumber, @RequestParam String packageCode){
        usersPhonesService.changePackageCode(phoneNumber, packageCode);
        String message = "The package code is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "UserPhone Controller", description = "Change a phone status")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "/phone/changePhoneStatus/{phoneNumber}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changePhoneStatus(@PathVariable String phoneNumber){
        usersPhonesService.changePhoneStatus(phoneNumber);
        String message = "The phone status is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "UserPhone Controller", description = "Get phone")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "phone/getPhone/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<PhoneViewDTO>> getPhone(@PathVariable String phoneNumber){
        PhoneViewDTO phoneViewDTO = usersPhonesService.getPhone(phoneNumber);
        return ResponseEntity.ok(RestAPIResponse.of(phoneViewDTO));
    }

    @Operation(tags = "UserPhone Controller", description = "Get phones by package code")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "phone/getPhonesByPackageCode/{packageCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PhoneViewDTO>>> getPhonesByPackageCode(@PathVariable String packageCode){
        List<PhoneViewDTO> phoneViewDTOList = usersPhonesService.getPhonesByPackageCode(packageCode);
        return ResponseEntity.ok(RestAPIResponse.of(phoneViewDTOList));
    }

    @Operation(tags = "UserPhone Controller", description = "Get all admin phones")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "phone/getAllAdminPhones" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PhoneViewDTO>>> getAllAdminPhones(){
        List<PhoneViewDTO> phoneViewDTOList = usersPhonesService.getAllAdminPhones();
        return ResponseEntity.ok(RestAPIResponse.of(phoneViewDTOList));
    }

    @Operation(tags = "UserPhone Controller", description = "Get all customers phones")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "phone/getAllCustomersPhones" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PhoneViewDTO>>> getAllCustomersPhones(){
        List<PhoneViewDTO> phoneViewDTOList = usersPhonesService.getAllCustomersPhones();
        return ResponseEntity.ok(RestAPIResponse.of(phoneViewDTOList));
    }

    @Operation(tags = "UserPhone Controller", description = "Save a new user")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/user/saveNewUser" , consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> saveUser(@RequestBody @Valid UserSaveDTO userSaveDto){
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        usersPhonesService.saveNewUser(userSaveDto);
        String message = "The user '" + userSaveDto.getUsername() + "' is saved.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "UserPhone Controller", description = "Get user by phone number")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/user/getUserByPhoneNumber/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<UserViewDTO>> getUserByPhoneNumber(@PathVariable String phoneNumber){
        UserViewDTO userViewDTO = usersPhonesService.getUserByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(RestAPIResponse.of(userViewDTO));
    }

    @Operation(tags = "UserPhone Controller", description = "Get user by username")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/user/getUserByUsername/{username}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<UserViewDTO>> getUserByUsername(@PathVariable String username){
        UserViewDTO userViewDTO = usersPhonesService.getUserByUsername(username);
        return ResponseEntity.ok(RestAPIResponse.of(userViewDTO));
    }

    @Operation(tags = "UserPhone Controller", description = "Get all admin users")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "user/getAllAdminUsers" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<UserViewDTO>>> getAllAdminUsers(){
        List<UserViewDTO> userViewDTOList = usersPhonesService.getAllAdminsUsers();
        return ResponseEntity.ok(RestAPIResponse.of(userViewDTOList));
    }

    @Operation(tags = "UserPhone Controller", description = "Get all customers users")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "user/getAllCustomersUsers" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<UserViewDTO>>> getAllCustomersUsers(){
        List<UserViewDTO> userViewDTOList = usersPhonesService.getAllCustomersUsers();
        return ResponseEntity.ok(RestAPIResponse.of(userViewDTOList));
    }

    @Operation(tags = "UserPhone Controller", description = "Change a user status")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "/user/changeUserStatus/{phoneNumber}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changeUserStatus(@PathVariable String phoneNumber){
        usersPhonesService.changeUserStatus(phoneNumber);
        String message = "The user status is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "UserPhone Controller", description = "Change a user password")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "/user/changeUserPassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changeUserPassword(@RequestBody @Valid UserChangePasswordDTO userChangePasswordDto){
        usersPhonesService.changeUserPassword(userChangePasswordDto);
        String message = "The user password is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "UserPhone Controller", description = "Delete a user")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "/user/deleteUser/{username}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> deleteUser(@PathVariable String username){
        usersPhonesService.deleteUser(username);
        String message = "The user '" +username + "' is deleted!";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

}
