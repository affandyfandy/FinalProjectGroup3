package com.hotel.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private final RsaKeyConfigProperties rsaKeyConfigProperties;

    @Autowired
    public SecurityConfig(RsaKeyConfigProperties rsaKeyConfigProperties) {
        this.rsaKeyConfigProperties = rsaKeyConfigProperties;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .pathMatchers("/api/v1/room/**").permitAll()
                        .pathMatchers("/api/v1/room/edit/**").hasAuthority("ADMIN")
                        .pathMatchers("/api/v1/room/create/**").hasAuthority("ADMIN")
                        .anyExchange().authenticated()
                )
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(Arrays.asList("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtSpec -> jwtSpec.jwtDecoder(jwtDecoder()))
                        .authenticationEntryPoint((exchange, ex) -> {
                            ServerHttpResponse response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            DataBuffer buffer = response.bufferFactory().wrap(
                                    ("{\"error\": \"Invalid Token\", \"message\": \"" + ex.getMessage() + "\"}").getBytes());
                            return response.writeWith(Mono.just(buffer));
                        })
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withPublicKey(rsaKeyConfigProperties.publicKey()).build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String role = jwt.getClaimAsString("role");
            if (role != null) {
                return Flux.just(new SimpleGrantedAuthority(role));
            }
            return Flux.empty();
        });

        return converter;
    }

}
