package com.snezana.introtelecom.exceptions;

public enum RestAPIErrorMessage implements GeneralErrorMessage{

    ITEM_NOT_FOUND("Item not found!"),
    WRONG_ITEM("Date could not be deleted!"),
    ITEM_IS_NOT_UNIQUE("Item is not unique!"),
    ITEMS_NOT_MATCH("Items not match!"),
    VALUE_CANNOT_BE_NEGATIVE("Value cannot be negative!"),
    PARAMETER_CANNOT_BE_NULL("Parameter cannot be null"),
            ;
//
    private String errorMessage;

    RestAPIErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}

