package com.snezana.introtelecom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@AllArgsConstructor
@Getter
@Setter
public class GeneralException extends RuntimeException{
    private RestAPIErrorMessage errorMessage;
    private String description;
}
