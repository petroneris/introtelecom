package com.snezana.introtelecom.securitycontext;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String username();
    String password();
}
