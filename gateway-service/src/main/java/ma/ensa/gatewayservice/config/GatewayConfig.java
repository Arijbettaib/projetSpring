package ma.ensa.gatewayservice.config;

import ma.ensa.gatewayservice.security.JwtGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder,
                               JwtGatewayFilter jwtGatewayFilter) {

        return builder.routes()

                // AUTH (PAS DE JWT)
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("lb://auth-service"))

                // PRODUCT
                .route("product-service", r -> r.path("/products/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("lb://product-service"))

                // STOCK
                .route("stock-service", r -> r.path("/stocks/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("lb://stock-service"))

                // MCP STOCK
                .route("stock-mcp", r -> r.path("/mcp/stock/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("lb://stock-service"))

                // AGENT IA
                .route("agent-ia", r -> r.path("/agents/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("lb://agent-ia-service"))

                .build();
    }
}