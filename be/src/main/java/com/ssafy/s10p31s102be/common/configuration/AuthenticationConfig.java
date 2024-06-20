package com.ssafy.s10p31s102be.common.configuration;

import com.ssafy.s10p31s102be.common.authentication.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider);
    }
}
