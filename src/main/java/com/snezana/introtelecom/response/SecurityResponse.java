package com.snezana.introtelecom.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class SecurityResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String,String> data;
    private String responseDate;
    private boolean isSuccess;

    public SecurityResponse(Map<String,String> data, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
        responseDate = LocalDateTime.now().toString();
    }

    public static SecurityResponse error(Map<String,String> data){
        return new SecurityResponse (data, false);
    }

}
