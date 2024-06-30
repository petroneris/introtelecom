package com.snezana.introtelecom.exception;

import com.snezana.introtelecom.response.RestAPIResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomRestAPIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleAll(Exception ex, WebRequest webRequest){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error_message", "ERROR OCCURED!");
        errorMap.put("description", ex.getLocalizedMessage());
        RestAPIResponse<Map<String, String>> restAPIResponse = RestAPIResponse.error(errorMap);
        return new ResponseEntity<>(restAPIResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAllGeneralException(GeneralException ex, WebRequest webRequest){
        RestAPIResponse<Map<String, String>> restAPIResponse = getErrorData(ex);
        return new ResponseEntity<>(restAPIResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAllItemNotFoundException(ItemNotFoundException ex, WebRequest webRequest){
        RestAPIResponse<Map<String, String>> restAPIResponse = getErrorData(ex);
        return new ResponseEntity<>(restAPIResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAllIllegalItemFieldException(IllegalItemFieldException ex, WebRequest webRequest){
        RestAPIResponse<Map<String, String>> restAPIResponse = getErrorData(ex);
        return new ResponseEntity<>(restAPIResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error_message","VALIDATION FAILED!");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        ex.getBindingResult().getGlobalErrors().forEach(error -> {
           errorMap.put(error.getObjectName(), error.getDefaultMessage());
        });
        RestAPIResponse<Map<String, String>> restAPIResponse = RestAPIResponse.error(errorMap);
        return new ResponseEntity<>(restAPIResponse, HttpStatus.BAD_REQUEST);
    }

    private RestAPIResponse<Map<String, String>> getErrorData (GeneralException ex){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error_message", ex.getErrorMessage().toString());
        errorMap.put("description", ex.getDescription());
        return RestAPIResponse.error(errorMap);
    }
}
