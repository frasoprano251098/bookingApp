package com.soprano.francesco.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Jwt2AuthenticationConverter authenticationConverter, ServerProperties serverProperties) throws Exception {

        return http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.POST, "/api/bookings").hasAnyAuthority("admin","user")
                                .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasAnyAuthority("admin","user")
                                .requestMatchers(HttpMethod.GET, "/api/rooms/available").hasAnyAuthority("admin","user")
                                .requestMatchers(HttpMethod.GET,"/api/bookings").hasAuthority("admin")
                                .requestMatchers("/api/rooms/**").hasAuthority("admin")
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                ).sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter)))
                .build();
    }

    public interface Jwt2AuthoritiesConverter extends Converter<Jwt, Collection<? extends GrantedAuthority>> {
    }

    @SuppressWarnings("unchecked")
    @Bean
    public Jwt2AuthoritiesConverter authoritiesConverter() {
        return jwt -> {
            final var realmAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("realm_access", Map.of());
            final var realmRoles = (Collection<String>) realmAccess.getOrDefault("roles", List.of());
            return realmRoles.stream().map(SimpleGrantedAuthority::new).toList();
        };
    }

    public interface Jwt2AuthenticationConverter extends Converter<Jwt, AbstractAuthenticationToken> {
    }

    @Bean
    public Jwt2AuthenticationConverter authenticationConverter(Jwt2AuthoritiesConverter authoritiesConverter) {
        return jwt -> {
            String username = jwt.getClaimAsString("preferred_username");
            if (username == null || username.isEmpty()) {
                username = jwt.getClaimAsString("sub");
            }

            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt));
            authenticationToken.setDetails(username); //

            return authenticationToken;
        };
    }



}
