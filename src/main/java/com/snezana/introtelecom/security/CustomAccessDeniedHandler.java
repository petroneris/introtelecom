package com.snezana.introtelecom.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snezana.introtelecom.response.SecurityResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/* If exception is thrown because user is not authorized for given resource, then CustomAccessDeniedHandler will be called.*/
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, String> errorMap = new TreeMap<>(Comparator.reverseOrder());
        errorMap.put("security_error_message", accessDeniedException.getMessage().toUpperCase());
        errorMap.put("access_denied_reason", "User is not authorized");
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), SecurityResponse.error(errorMap));
    }
}
