package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.validation.FieldMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(baseField = "phoneNumber", matchField = "checkPhoneNumber", message = "The phoneNumber and checkPhoneNumber fields must match")
public class PhoneSaveDTO {

    @NotBlank
    @Pattern(regexp = "0[1-9][0-9]{8}", message = "Mobile phone number must have 10 digits, with leading 0 and the second number in range [1-9]")
    private String phoneNumber;
    @NotBlank
    private String checkPhoneNumber;
    @NotBlank
    private String packageCode;

}
