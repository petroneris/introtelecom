package com.snezana.introtelecom.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@AllArgsConstructor
@Getter
@Setter
public class GeneralException extends RuntimeException{
    private RestAPIErrorMessage errorMessage;
    private String description;
}
