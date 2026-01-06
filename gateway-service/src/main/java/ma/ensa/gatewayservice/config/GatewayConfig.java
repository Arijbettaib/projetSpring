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

                // PRODUCT - with /api prefix
                .route("product-service", r -> r.path("/api/products/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("lb://product-service"))

                // STOCK - with /api prefix
                .route("stock-service", r -> r.path("/api/stocks/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("lb://stock-service"))

                // MCP PRODUCT
                .route("product-mcp", r -> r.path("/mcp/product/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("lb://product-service"))

                // MCP STOCK
                .route("stock-mcp", r -> r.path("/mcp/stock/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("lb://stock-service"))

                // AGENT IA
                .route("agent-ia", r -> r.path("/api/agent/**", "/chat/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("lb://agent-ia-service"))

                .build();
    }
}