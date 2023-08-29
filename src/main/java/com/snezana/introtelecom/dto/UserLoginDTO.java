package com.snezana.introtelecom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserLoginDTO {
    @NotBlank
    private String username;
    @NotBlank
    @Schema(format = "password")
    private String password;

}
