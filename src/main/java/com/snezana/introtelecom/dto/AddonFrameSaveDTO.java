package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddonFrameSaveDTO {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String addonCode;

}
