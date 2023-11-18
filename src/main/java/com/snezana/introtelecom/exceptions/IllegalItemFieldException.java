package com.snezana.introtelecom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalItemFieldException extends GeneralException{

    public IllegalItemFieldException(RestAPIErrorMessage errorMessage, String description) {
        super(errorMessage, description);
    }
}
