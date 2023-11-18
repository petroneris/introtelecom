package com.snezana.introtelecom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserViewDTO {

    private String phoneNumber;
    private String username;
    private String roleType;
    private String userStatus;
}
