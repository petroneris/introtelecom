package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.entity.Phone;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminViewDTO {

    private Long adminId;
    private String personalNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}

