package com.snezana.introtelecom.enums;

public enum RoleType {
    ADMIN("ADMIN"), CUSTOMER("CUSTOMER");

    private  String role;

    private RoleType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
