package com.snezana.introtelecom.security;

import com.snezana.introtelecom.enums.BearerConstant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

/*This filter will read and validate the token given as input by the user in the header with key 'Authorization'*/
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JWTtokenGenerator jwTtokenGenerator;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if (StringUtils.hasText(token)) {
            boolean isValid = jwTtokenGenerator.validateToken(token);
            if (isValid) {
                String username = jwTtokenGenerator.findUsernameByToken(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
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
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(authorizationHeader)){
            String bearer = BearerConstant.BEARER.getBconst();
            if (authorizationHeader.startsWith(bearer)){
                token = authorizationHeader.substring(bearer.length());
            }
        }
        return token;
    }

}
