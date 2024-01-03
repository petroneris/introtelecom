package com.snezana.introtelecom.enums;

import java.util.HashMap;
import java.util.Map;

public enum PackagePlanType {

    ADM("00"), PRP01("01"), PRP02("02"), PST11("11"), PST12("12"), PST13("13"), PST14("14");

    private final String packageCode;

    private PackagePlanType(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageCode() {
        return packageCode;
    }

    private static final Map<String, PackagePlanType> map;

    static {
        map = new HashMap <String, PackagePlanType>();
        for (PackagePlanType v : PackagePlanType.values()) {
            map.put(v.packageCode, v);
        }
    }
    public static PackagePlanType findByKey(String s) {
        return map.get(s);
    }

}
