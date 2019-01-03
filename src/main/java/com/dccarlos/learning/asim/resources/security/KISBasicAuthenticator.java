package com.dccarlos.learning.asim.resources.security;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class KISBasicAuthenticator implements Authenticator<BasicCredentials, KisUser> {

    /* This is ONLY for *DEMO* purposes */
    private static final List<String> adminUsers = Arrays.asList(
            "kis-user"
            );
    private static final String adminPassword = "ultraSecret4u";

    @Override
    public Optional<KisUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        log.warn("Authenticate: {}", credentials);
        
        final String userName = credentials.getUsername();
        final String password = credentials.getPassword();
        if (userName != null && !userName.trim().isEmpty()) {
            if ((adminUsers.contains(userName) && password.equals(adminPassword)))
                return Optional.of(KisUser.builder().userName(userName).build());
            else
                return Optional.empty();
        } else
            return Optional.empty();
    }
}