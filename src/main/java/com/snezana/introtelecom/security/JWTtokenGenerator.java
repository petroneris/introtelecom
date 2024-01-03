package com.snezana.introtelecom.security;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Date;

@Component
public class JWTtokenGenerator {

    @Value("${introtelecom.jwt.app.key}")
    private String APP_KEY;

    @Value("${introtelecom.jwt.expire.time.acc}")
    private Long EXPIRE_TIME_ACC;

    public String generateAccess_Token (Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRE_TIME_ACC);
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, APP_KEY)
                .claim("role", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .compact();
        return token;
    }

    public Long findUserByToken(String token){
        Jws<Claims> claimsJws = parseToken(token);
        String userIdStr = claimsJws
                .getBody()
                .getSubject();
        return Long.parseLong(userIdStr);
    }

    public String findUsernameByToken(String token){
        Jws<Claims> claimsJws = parseToken(token);
        String username = claimsJws
                .getBody()
                .getSubject();
        return username;
    }

    private Jws<Claims> parseToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(APP_KEY)
                .parseClaimsJws(token);
        return claimsJws;
    }

    public boolean validateToken(String token){
        boolean isValid;
        try {
            Jws<Claims> claimsJws = parseToken(token);
            isValid = !isTokenExpired(claimsJws);
        } catch (Exception e){
            isValid = false;
        }
        return isValid;
    }

    private boolean isTokenExpired(Jws<Claims> claimsJws) {
        Date expirationDate = claimsJws.getBody().getExpiration();
        return expirationDate.before(new Date());
    }

}
