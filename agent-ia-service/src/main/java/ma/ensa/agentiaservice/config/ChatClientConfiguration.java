package ma.ensa.agentiaservice.config;

import ma.ensa.agentiaservice.tools.ProductTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {

    @Bean
    public ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository repository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ProductTools productTools, ChatMemory chatMemory) {
        System.out.println("DEBUG: Registering ProductTools directly with ChatClient");
        
        return builder
                .defaultSystem("You are a helpful AI assistant for managing products and inventory. " +
                        "You have access to tools to interact with the product database. " +
                        "ALWAYS use the get_all_products tool when users ask to list or see products.")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(productTools)
                .build();
    }
}
