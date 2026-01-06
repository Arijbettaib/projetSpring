package ma.ensa.agentiaservice.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration for MCP Clients connecting to microservices
 * 
 * MCP clients are auto-configured via application.yml:
 * - product-service-mcp: connects to Product Service MCP server
 * - stock-service-mcp: connects to Stock Service MCP server
 * 
 * The ToolCallbackProvider beans are automatically created by Spring AI
 * and injected into ChatClientConfiguration
 */
@Configuration
public class McpClientConfiguration {
    // MCP clients are auto-configured via spring.ai.mcp.client.sse properties
}
