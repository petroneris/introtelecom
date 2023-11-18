package com.snezana.introtelecom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends GeneralException{

    public ItemNotFoundException(RestAPIErrorMessage errorMessage, String description) {
        super(errorMessage, description);
    }
}
