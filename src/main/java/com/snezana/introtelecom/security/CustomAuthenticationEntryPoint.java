package com.snezana.introtelecom.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snezana.introtelecom.response.SecurityResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * If the user is not authenticated, an exception is thrown and this method will be called
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, String> errorMap = new TreeMap<>(Comparator.reverseOrder());
        errorMap.put("security_error_message", "AUTHENTICATION REQUIRED" );
        errorMap.put("access_denied_reason", authException.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), SecurityResponse.error(errorMap));
    }
}

