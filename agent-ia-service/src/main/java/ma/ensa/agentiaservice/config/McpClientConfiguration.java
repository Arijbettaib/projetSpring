package ma.ensa.agentiaservice.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration for MCP Clients connecting to microservices
 * Stub implementation - waiting for Spring AI starters to be resolved
 * 
 * Once spring-ai-starter-mcp-client is available, this will configure:
 * - MCP client for Product Service (access to product tools)
 * - MCP client for Stock Service (access to inventory tools)
 * - HTTP transport layer for server communication
 */
@Configuration
public class McpClientConfiguration {

    // Configuration properties will be added once dependencies are resolved
    // @Value("${mcp.product-service.url:http://localhost:9091/mcp}")
    // private String productServiceUrl;
    //
    // @Value("${mcp.stock-service.url:http://localhost:9092/mcp}")
    // private String stockServiceUrl;
    //
    // @Bean(name = "productServiceMcpClient")
    // public McpClient productServiceMcpClient() {
    //     return McpClient.builder()
    //             .name("ProductServiceMcpClient")
    //             .transport(McpClientStreamingTransport.httpBuilder()
    //                     .url(productServiceUrl)
    //                     .build())
    //             .build();
    // }
    //
    // @Bean(name = "stockServiceMcpClient")
    // public McpClient stockServiceMcpClient() {
    //     return McpClient.builder()
    //             .name("StockServiceMcpClient")
    //             .transport(McpClientStreamingTransport.httpBuilder()
    //                     .url(stockServiceUrl)
    //                     .build())
    //             .build();
    // }
}
