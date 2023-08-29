package com.snezana.introtelecom.security;

import com.snezana.introtelecom.enums.BearerConstant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CustomAuthorizationFilter.class);

    @Autowired
    private JWTtokenGenerator jwTtokenGenerator;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        log.info("Token is: {}", token);
        if (StringUtils.hasText(token)) {
            log.info("StringUtils.hasText(token)", StringUtils.hasText(token));
            boolean isValid = jwTtokenGenerator.validateToken(token);
            log.info("isValid = {}", isValid);
            if (isValid) {
                String username = jwTtokenGenerator.findUsernameByToken(token);
                log.info("Username is: {}", username);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
//                        if (userDetails != null && userDetails.getAuthorities().stream()
//                                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//                            log.info("User has role ADMIN");
//                        }
                if (username != null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            }
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String fullToken = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(fullToken)){
            String bearer = BearerConstant.BEARER.getBconst();
            if (fullToken.startsWith(bearer)){
                token = fullToken.substring(bearer.length());
            }
        }
        return token;
    }
}
