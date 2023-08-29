package com.snezana.introtelecom.enums;

public enum StatusType {
    ACTIVE("ACTIVE"), INACTIVE("INACTIVE");
    private String status;

    private  StatusType(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
