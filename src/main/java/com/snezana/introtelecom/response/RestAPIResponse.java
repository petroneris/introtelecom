package com.snezana.introtelecom.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class RestAPIResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private T data;
    private LocalDateTime responseDate;
    private boolean isSuccess;
//    private String message;

    public RestAPIResponse(T data, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
        responseDate = LocalDateTime.now();
    }

    public static <T> RestAPIResponse<T> of(T t){
        return new RestAPIResponse<>(t, true);
    }

    public static <T> RestAPIResponse<T> error(T t){
        return new RestAPIResponse<>(t, false);
    }

    public static <T> RestAPIResponse<T> empty(){
        return new RestAPIResponse<>(null, true);
    }

//    public void setStatus(HttpStatus message) {
//        this.message = message;
//    }
//
//    public String getMessage(){
//        return message;
//    }

}
