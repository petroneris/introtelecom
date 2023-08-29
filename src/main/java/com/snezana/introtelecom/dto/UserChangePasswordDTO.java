package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.validations.FieldMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(baseField = "newPassword", matchField = "checkNewPassword", message = "The newPassword and checkNewPassword fields must match")
public class UserChangePasswordDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String checkNewPassword;

}