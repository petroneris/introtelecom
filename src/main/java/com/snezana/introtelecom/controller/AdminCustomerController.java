package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.AdminSaveDTO;
import com.snezana.introtelecom.dto.AdminViewDTO;
import com.snezana.introtelecom.dto.CustomerSaveDTO;
import com.snezana.introtelecom.dto.CustomerViewDTO;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.response.RestMessage;
import com.snezana.introtelecom.service.AdminCustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
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
public class AdminCustomerController {

//    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminCustomerController.class);
    private final AdminCustomerService adminCustomerService;

    @Operation(tags = "AdminCustomer Controller", description = "Save a new admin")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/admin/saveNewAdmin", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> saveAdmin(@RequestBody @Valid AdminSaveDTO adminSaveDto){
        adminCustomerService.saveNewAdmin(adminSaveDto);
        String message = "New admin is saved.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Update admin")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "/admin/updateAdmin/{adminId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> updateAdmin(@RequestBody @Valid AdminSaveDTO adminSaveDto, @PathVariable Long adminId){
        adminCustomerService.updateAdmin(adminSaveDto, adminId);
        String message = "The admin is updated.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get admin by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "admin/getAdminById/{adminId}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<AdminViewDTO>> getAdminById(@PathVariable Long adminId){
        AdminViewDTO adminViewDTO = adminCustomerService.getAdminById(adminId);
        return ResponseEntity.ok(RestAPIResponse.of(adminViewDTO));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get admin by phone number")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "admin/getAdminByPhoneNumber/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<AdminViewDTO>> getAdminByPhoneNumber(@PathVariable String phoneNumber){
        AdminViewDTO adminViewDTO = adminCustomerService.getAdminByPhone(phoneNumber);
        return ResponseEntity.ok(RestAPIResponse.of(adminViewDTO));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get admin by personal number")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "admin/getAdminByPersonalNumber/{personalNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<AdminViewDTO>> getAdminByPersonalNumber(@PathVariable String personalNumber){
        AdminViewDTO adminViewDTO = adminCustomerService.getAdminByPersonalNumber(personalNumber);
        return ResponseEntity.ok(RestAPIResponse.of(adminViewDTO));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get admin by email")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "admin/getAdminByEmail/{email}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<AdminViewDTO>> getAdminByEmail(@PathVariable String email){
        AdminViewDTO adminViewDTO = adminCustomerService.getAdminByEmail(email);
        return ResponseEntity.ok(RestAPIResponse.of(adminViewDTO));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get all admins")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "admin/getAllAdmins" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<AdminViewDTO>>> getAllAdmins(){
        List<AdminViewDTO> adminViewDTOList = adminCustomerService.getAllAdmins();
        return ResponseEntity.ok(RestAPIResponse.of(adminViewDTOList));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Save a new customer")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/customer/saveNewCustomer", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> saveCustomer(@RequestBody @Valid CustomerSaveDTO customerSaveDto){
        adminCustomerService.saveNewCustomer(customerSaveDto);
        String message = "New customer is saved.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Update customer")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "/customer/updateCustomer/{customerId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> updateCustomer(@RequestBody @Valid CustomerSaveDTO customerSaveDto, @PathVariable Long customerId){
        adminCustomerService.updateCustomer(customerSaveDto, customerId);
        String message = "The customer is updated.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get customer by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "customer/getCustomerById/{customerId}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<CustomerViewDTO>> getCustomerById(@PathVariable Long customerId){
        CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerById(customerId);
        return ResponseEntity.ok(RestAPIResponse.of(customerViewDTO));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get customer by personal number")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "customer/getCustomerByPersonalNumber/{personalNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<CustomerViewDTO>> getCustomerByPersonalNumber(@PathVariable String personalNumber){
        CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerByPersonalNumber(personalNumber);
        return ResponseEntity.ok(RestAPIResponse.of(customerViewDTO));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get customer by email")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "customer/getCustomerByEmail/{email}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<CustomerViewDTO>> getCustomerByEmail(@PathVariable String email){
        CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerByEmail(email);
        return ResponseEntity.ok(RestAPIResponse.of(customerViewDTO));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get all customers")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "customer/getAllCustomers" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<CustomerViewDTO>>> getAllCustomers(){
        List<CustomerViewDTO> customerViewDTOList = adminCustomerService.getAllCustomers();
        return ResponseEntity.ok(RestAPIResponse.of(customerViewDTOList));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get customer by username")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "customer/getCustomerByUsername/{username}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<CustomerViewDTO>> getCustomerByUsername(@PathVariable String username){
        CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerByUsername(username);
        return ResponseEntity.ok(RestAPIResponse.of(customerViewDTO));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Get customer by phone")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "customer/getCustomerByPhone/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<CustomerViewDTO>> getCustomerByPhone(@PathVariable String phoneNumber){
        CustomerViewDTO customerViewDTO = adminCustomerService.getCustomerByPhone(phoneNumber);
        return ResponseEntity.ok(RestAPIResponse.of(customerViewDTO));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Add phone to a customer")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "/customer/addPhoneToCustomer/{customerId}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> addPhoneToCustomer(@PathVariable Long customerId , @RequestParam String phoneNumber){
        adminCustomerService.addPhoneToCustomer(customerId, phoneNumber);
        String message = "The phone is added to customer.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "AdminCustomer Controller", description = "Remove phone from a customer")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "/customer/removePhoneFromCustomer/{customerId}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> removePhoneFromCustomer(@PathVariable Long customerId , @RequestParam String phoneNumber){
        adminCustomerService.removePhoneFromCustomer(customerId, phoneNumber);
        String message = "The phone is removed from customer.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

}
