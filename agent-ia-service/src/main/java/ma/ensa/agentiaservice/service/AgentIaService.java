package ma.ensa.agentiaservice.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * AI Agent Service using Spring AI ChatClient with MCP tools
 * Orchestrates interactions between the LLM and product/stock services
 */
@Service
public class AgentIaService {

    private final ChatClient chatClient;
    private final List<String> conversationHistory = new ArrayList<>();

    public AgentIaService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String processUserInput(String userInput) {
        conversationHistory.add("USER: " + userInput);
        
        try {
            String response = chatClient.prompt()
                    .user(userInput)
                    .call()
                    .content();
            
            conversationHistory.add("ASSISTANT: " + response);
            return response;
        } catch (Exception e) {
            String errorMsg = "Error processing request: " + e.getMessage();
            conversationHistory.add("ASSISTANT: " + errorMsg);
            return errorMsg;
        }
    }

    public Flux<String> streamUserInput(String userInput, String role) {
        conversationHistory.add("USER: " + userInput + " [ROLE: " + role + "]");
        
        String systemPrompt = "You are a helpful AI Assistant for product and inventory management. " +
                "CRITICAL: You MUST ALWAYS use the available tools to retrieve real-time data from the database. " +
                "NEVER make up or hallucinate product information. ";
        
        if ("ADMIN".equals(role)) {
            systemPrompt += "You have FULL ACCESS. You can Create, Read, Update, and Delete products and stocks. " +
                    "When users ask about products, ALWAYS call the appropriate tool to get real data.";
        } else {
            systemPrompt += "You are interacting with a standard USER. " +
                    "Your permissions are: [READ-ONLY]. " +
                    "You ARE AUTHORIZED and REQUIRED to use tools to LIST, SEARCH, and VIEW products and stocks. " +
                    "You are STRICTLY FORBIDDEN from creating, updating, or deleting data. " +
                    "If the user asks to modify data, refuse politely. " +
                    "When users ask about products, ALWAYS call the list/search tools to get real data from the database.";
        }

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userInput)
                .stream()
                .content();
    }

    public List<String> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }

    public void clearHistory() {
        conversationHistory.clear();
    }

    public String getAvailableTools() {
        return "Connected to MCP Servers:\n" +
                "  - product-service-mcp (Product Management Tools)\n" +
                "  - stock-service-mcp (Stock Management Tools)\n" +
                "\nAvailable operations:\n" +
                "  Products: create, get, update, delete, search by price\n" +
                "  Stock: check availability, update quantity, decrease stock";
    }
}
