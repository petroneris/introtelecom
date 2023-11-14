package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.validations.FieldMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(baseField = "password", matchField = "checkPassword", message = "The password and checkPassword fields must match")
public class UserSaveDTO {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String checkPassword;

    @NotBlank
    private String roleType;


}
