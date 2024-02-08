package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserLoginDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
