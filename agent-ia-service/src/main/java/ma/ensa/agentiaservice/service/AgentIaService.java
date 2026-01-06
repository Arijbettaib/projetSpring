package ma.ensa.agentiaservice.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Stub Agent IA Service (waiting for Spring AI starters to be resolved)
 * This service will eventually integrate with ChatClient and MCP clients
 * for AI orchestration with product and inventory management tools.
 */
@Service
public class AgentIaService {

    private final List<String> conversationHistory = new ArrayList<>();

    public String processUserInput(String userInput) {
        // Stub: store message and return placeholder response
        conversationHistory.add("USER: " + userInput);
        String response = "Agent IA Service not yet fully integrated. " +
                "Waiting for Spring AI dependencies. Please ensure Ollama is running on http://localhost:11434";
        conversationHistory.add("ASSISTANT: " + response);
        return response;
    }

    public List<String> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }

    public void clearHistory() {
        conversationHistory.clear();
    }

    public String getAvailableTools() {
        return "Tools from Product Service:\n" +
                "  - create_product\n" +
                "  - get_all_products\n" +
                "  - get_product_by_id\n" +
                "  - search_products_by_price\n" +
                "  - update_product_price\n" +
                "  - get_product_info\n" +
                "  - delete_product\n" +
                "  - count_products\n" +
                "\nTools from Stock Service:\n" +
                "  - get_stock_by_product\n" +
                "  - update_stock_quantity\n" +
                "  - check_stock_availability\n" +
                "  - decrease_stock\n" +
                "  - get_all_stocks\n" +
                "  - create_stock_record\n" +
                "\n[Note: Full MCP integration pending Spring AI dependency resolution]";
    }
}
