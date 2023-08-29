package com.snezana.introtelecom.enums;

public enum PackageCodeType {
    ADM("00"), PRP01("01"), PRP02("02"), PST11("11"), PST12("12"), PST13("13"), PST14("14");

    private String packageCode;

    private PackageCodeType(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageCode() {
        return packageCode;
    }
}
