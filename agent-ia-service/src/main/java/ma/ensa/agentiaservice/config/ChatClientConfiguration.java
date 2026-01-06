package ma.ensa.agentiaservice.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Spring AI ChatClient
 * Stub implementation - waiting for Spring AI starters to be resolved
 * 
 * Once spring-ai-starter-ollama is available, this will configure:
 * - OllamaChatClient bean
 * - Connection to Ollama LLM server
 */
@Configuration
public class ChatClientConfiguration {

    // Configuration properties will be added once dependencies are resolved
    // @Value("${spring.ai.ollama.base-url:http://localhost:11434}")
    // private String ollamaBaseUrl;
    //
    // @Value("${spring.ai.ollama.model:llama2}")
    // private String model;
    //
    // @Bean
    // public ChatClient chatClient() {
    //     OllamaApi ollamaApi = new OllamaApi(ollamaBaseUrl);
    //     OllamaChatClient ollamaChatClient = new OllamaChatClient(ollamaApi)
    //             .withModel(model);
    //     return ChatClient.builder(ollamaChatClient).build();
    // }
}
