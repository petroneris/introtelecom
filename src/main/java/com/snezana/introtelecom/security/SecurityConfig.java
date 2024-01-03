package com.snezana.introtelecom.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTtokenGenerator jwTtokenGenerator;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public CustomAuthorizationFilter customAuthorizationFilter(){
        return new CustomAuthorizationFilter(jwTtokenGenerator, customUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers("/access/**", "/swagger-ui-custom.html" ,"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**",
                                "/swagger-ui/index.html","/api-docs/**")
                        .permitAll()
                        .antMatchers("/api/user/**").hasRole("ADMIN")
                        .antMatchers("/api/phone/**").hasRole("ADMIN")
                        .antMatchers("/api/admin/**").hasRole("ADMIN")
                        .antMatchers("/api/customer/**").hasRole("ADMIN")
                        .antMatchers("/api/sdr/**").hasRole("ADMIN")
                        .antMatchers("/api/packageFrame/**").hasRole("ADMIN")
                        .antMatchers("/api/addonFrame/**").hasRole("ADMIN")
                        .antMatchers("/api/addon/**").hasRole("ADMIN")
                        .antMatchers("/api/service/**").hasRole("ADMIN")
                        .antMatchers("/api/package/**").hasRole("ADMIN")
                        .antMatchers("/api/monthlyBillFacts/**").hasRole("ADMIN")
                        .antMatchers("/api/currentInfo/**").hasRole("ADMIN")
                        .antMatchers("/api/client/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated())
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
