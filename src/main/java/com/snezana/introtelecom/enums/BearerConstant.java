package com.snezana.introtelecom.enums;

public enum BearerConstant {
    BEARER("Bearer ");

    private final String bconst;

    BearerConstant(String bconst) {
        this.bconst = bconst;
    }

    public String getBconst() {
        return bconst;
    }

    @Override
    public String toString() {
        return "BearerConstant{" +
                "bconst='" + bconst + '\'' +
                '}';
    }
}
