package ma.ensa.agentiaservice.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

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
