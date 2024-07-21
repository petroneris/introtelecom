package com.snezana.introtelecom.enums;

public enum StatusType {

    PRESENT("ACTIVE"), NOT_IN_USE("INACTIVE");

    private String status;

    StatusType(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
