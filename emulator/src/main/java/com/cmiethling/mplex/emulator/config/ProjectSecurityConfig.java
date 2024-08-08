package com.cmiethling.mplex.emulator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class ProjectSecurityConfig {

    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http,
                                                   final HandlerMappingIntrospector introspector) throws Exception {
        http.authorizeHttpRequests(request -> request.anyRequest().permitAll());
        http.httpBasic(Customizer.withDefaults());
        http.formLogin(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    // 116, ex29: mehrere users
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        final var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // User.withDefaultPasswordEncoder();
        final var user = User.withUsername("user")
                .password(encoder.encode("user"))
                .roles(USER_ROLE)
                .build();
        final var admin = User.withUsername("admin")
                .password(encoder.encode("admin"))
                .roles(ADMIN_ROLE)
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }
}
