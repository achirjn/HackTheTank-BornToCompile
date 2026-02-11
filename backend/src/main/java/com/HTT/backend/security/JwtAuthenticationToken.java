package com.HTT.backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken{

    private final String token;

    public JwtAuthenticationToken(String token) {
        super(java.util.Collections.emptyList());
        this.token = token;
        setAuthenticated(false);
    }

    public String getToken(){
        return token;
    }
    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}

