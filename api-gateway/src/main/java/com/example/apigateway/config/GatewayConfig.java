package com.example.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthGatewayFilterFactory jwtAuthFilter;

    @Value("${auth.service.url}")
    private String AUTH_SERVICE_URL;

    @Value("${task.service.url}")
    private String TASK_SERVICE_URL;


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                      // 🔐 Auth service routing
            .route(
                  "auth-service", r -> r.path("/auth/**")
                                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthGatewayFilterFactory.Config())))
                                        .uri(AUTH_SERVICE_URL))

            // 📋 Task service routing
            .route(
                  "task-service", r -> r.path("/api/task/**")
                                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthGatewayFilterFactory.Config())))
                                        .uri(TASK_SERVICE_URL))

            .build();
    }
}
