package ma.ensa.agentiaservice.controller;

import ma.ensa.agentiaservice.model.ChatRequest;
import ma.ensa.agentiaservice.model.ChatResponse;
import ma.ensa.agentiaservice.service.AgentIaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for interacting with the AI Agent
 */
@RestController
@RequestMapping("/api/agent")
@CrossOrigin(origins = "*")
public class AgentController {

    private final AgentIaService agentService;

    public AgentController(AgentIaService agentService) {
        this.agentService = agentService;
    }

    /**
     * Send a message to the AI agent and get a response
     * POST /api/agent/chat
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            String response = agentService.processUserInput(request.getMessage());
            return ResponseEntity.ok(new ChatResponse(response, true));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ChatResponse("Error: " + e.getMessage(), false));
        }
    }

    /**
     * Get the conversation history
     * GET /api/agent/history
     */
    @GetMapping("/history")
    public ResponseEntity<List<String>> getHistory() {
        return ResponseEntity.ok(agentService.getConversationHistory());
    }

    /**
     * Clear the conversation history
     * DELETE /api/agent/history
     */
    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory() {
        agentService.clearHistory();
        return ResponseEntity.ok().build();
    }

    /**
     * Get available tools
     * GET /api/agent/tools
     */
    @GetMapping("/tools")
    public ResponseEntity<String> getTools() {
        return ResponseEntity.ok(agentService.getAvailableTools());
    }

    /**
     * Health check endpoint
     * GET /api/agent/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("AI Agent Service is running");
    }
}
