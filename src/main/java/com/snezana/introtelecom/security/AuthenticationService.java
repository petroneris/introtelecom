package com.snezana.introtelecom.security;

import com.snezana.introtelecom.dto.UserLoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Grants a JWT access token to the user (with valid username and valid password)
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JWTtokenGenerator jwTtokenGenerator;
    private final AuthenticationManager authenticationManager;

    public Map<String, String> login(UserLoginDTO userLoginDto) {
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String access_token = jwTtokenGenerator.generateAccess_Token(authentication);
        Map<String, String> token = new HashMap<>();
        token.put("access_token", access_token);
        return token;
    }
}
