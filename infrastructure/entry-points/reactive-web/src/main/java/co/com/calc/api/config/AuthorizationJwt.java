package co.com.calc.api.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Log4j2
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class AuthorizationJwt implements WebFluxConfigurer {
    private final String jsonExpRoles;
    private final String jwtSecret;
    private final ObjectMapper mapper;
    private static final String ROLE = "ROLE_";

public AuthorizationJwt(@Value("${spring.security.oauth2.resourceserver.jwt.secret-key}") String jwtSecret,
                        @Value("${jwt.json-exp-roles}") String jsonExpRoles,
                        ObjectMapper mapper) {
    this.jwtSecret = jwtSecret;
    this.jsonExpRoles = jsonExpRoles;
    this.mapper = mapper;
}

@Bean
public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
    http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange ->
                    exchange
                            .pathMatchers("/api/auth/register", "/api/auth/login").permitAll()
                            .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwtSpec ->
                            jwtSpec
                                    .jwtDecoder(reactiveJwtDecoder())
                                    .jwtAuthenticationConverter(grantedAuthoritiesExtractor())
                    )
            );
    return http.build();
}

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return token -> Mono.fromCallable(() -> {
            try {
                io.jsonwebtoken.Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                Claims claims = jws.getBody();
                JwsHeader<?> header = jws.getHeader();
                Instant issuedAt = claims.getIssuedAt().toInstant();
                Instant expiresAt = claims.getExpiration().toInstant();
                return new Jwt(token, issuedAt, expiresAt, header, claims);
            } catch (ExpiredJwtException e) {
                log.warn("Token vencido: {}", e.getMessage());
                throw new BadJwtException("Token vencido", e);
            } catch (io.jsonwebtoken.JwtException e) {
                log.error("Token incorrecto: {}", e.getMessage());
                throw new BadJwtException("Token incorrecto", e);
            }
        });
    }
public Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
    var jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(jwt ->
            getRoles(jwt.getClaims(), jsonExpRoles)
                    .stream()
                    .map(ROLE::concat)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));
    return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
}

private List<String> getRoles(Map<String, Object> claims, String jsonExpClaim){
    List<String> roles = List.of();
    try {
        var json = mapper.writeValueAsString(claims);
        var chunk = mapper.readTree(json).at(jsonExpClaim);
        return mapper.readerFor(new TypeReference<List<String>>() {})
                .readValue(chunk);
    } catch (IOException e) {
        log.error(e.getMessage());
        return roles;
    }
}
}