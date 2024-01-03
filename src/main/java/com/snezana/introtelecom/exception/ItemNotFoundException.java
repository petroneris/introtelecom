package com.snezana.introtelecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends GeneralException{

    public ItemNotFoundException(RestAPIErrorMessage errorMessage, String description) {
        super(errorMessage, description);
    }
}
