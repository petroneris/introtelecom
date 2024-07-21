package com.snezana.introtelecom.enums;

public enum RoleType {

    ADM("ADMIN"), CSTM("CUSTOMER");

    private  String role;

    RoleType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
