package com.snezana.introtelecom.security;

import com.snezana.introtelecom.dto.UserLoginDTO;
import com.snezana.introtelecom.enums.BearerConstant;
import com.snezana.introtelecom.security.JWTtokenGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.util.HashMap;

import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private JWTtokenGenerator jwTtokenGenerator;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Map<String, String> login(UserLoginDTO userLoginDto) {
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();
//        log.info("Username is: {}", username);
//        log.info("password is: {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String bearer = BearerConstant.BEARER.getBconst();
        String access_token = jwTtokenGenerator.generateAccess_Token(authentication);
        Map<String, String> token = new HashMap<>();
        token.put("access_token", access_token);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        return token;
    }
}
