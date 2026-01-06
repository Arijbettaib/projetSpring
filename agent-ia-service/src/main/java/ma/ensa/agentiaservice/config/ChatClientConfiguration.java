package ma.ensa.agentiaservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for Spring AI ChatClient with MCP tools
 */
@Configuration
public class ChatClientConfiguration {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, List<ToolCallbackProvider> toolCallbackProviders) {
        var chatClientBuilder = builder
                .defaultSystem("You are a helpful AI assistant for managing products and inventory. " +
                        "You can help users create, update, delete products and manage stock levels. " +
                        "Use the available tools to interact with the product and stock databases.");
        
        // Register all MCP tool providers
        for (ToolCallbackProvider provider : toolCallbackProviders) {
            chatClientBuilder.defaultTools(provider);
        }
        
        return chatClientBuilder.build();
    }
}
