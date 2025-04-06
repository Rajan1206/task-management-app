package com.example.apigateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;

@Slf4j
@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    @Value("${jwt.secret}")
    private String jwtSecret;


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // Skip JWT validation for public endpoints
            if (path.startsWith("/auth/login") || path.startsWith("/auth/signup")) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            try {
                byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(jwtSecret);


                Claims claims = Jwts.parser()
                                    .setSigningKey(secretKeyBytes)
                                    .parseClaimsJws(token)
                                    .getBody();

                // Add userId & email Id as headers for downstream services
                exchange.getRequest().mutate()
                        .header("X-USER-ID", claims.getIssuer())
                        .header("X-User-Email", claims.get("emailId", String.class))
                        .build();

                return chain.filter(exchange);

            } catch (Exception e) {
                log.error("JWT validation failed", e);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        };
    }

    public static class Config { }
}
