package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.validations.FieldMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(baseField = "phoneNumber", matchField = "checkPhoneNumber", message = "The phoneNumber and checkPhoneNumber fields must match")
public class PhoneSaveDTO {

    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String checkPhoneNumber;
    @NotBlank
    private String packageCode;

}
