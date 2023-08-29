package com.snezana.introtelecom.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
//@NoArgsConstructor
public class RestMessage {

    public static Map<String, String> view (String message) {
        Map<String, String> newMap = new HashMap<>();
        newMap.put("message", message);
        return newMap;
    }

}
