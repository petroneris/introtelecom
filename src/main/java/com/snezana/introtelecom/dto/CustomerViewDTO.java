package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerViewDTO {

    private Long customerId;
    private String personalNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private Set<String> phones;
}
