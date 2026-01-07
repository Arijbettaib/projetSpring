package ma.ensa.agentiaservice.controller;

import ma.ensa.agentiaservice.model.ChatRequest;
import ma.ensa.agentiaservice.model.ChatResponse;
import ma.ensa.agentiaservice.service.AgentIaService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * REST Controller for interacting with the AI Agent
 */
@RestController
@RequestMapping("/api/agent")
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
     * Send a message to the AI agent and get a streaming response
     * POST /api/agent/chat/stream
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody ChatRequest request, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String role = extractRoleFromToken(authHeader);
        try {
            return agentService.streamUserInput(request.getMessage(), role)
                    .onErrorResume(e -> {
                        String errorMsg = "⚠️ Désolé, je ne peux pas me connecter à mon cerveau (Ollama) pour le moment. " +
                                "Vérifiez que le service Ollama est bien lancé sur le port 11434. " +
                                "Détails : " + e.getMessage();
                        return Flux.just(errorMsg);
                    });
        } catch (Exception e) {
            String errorMsg = "⚠️ Désolé, une erreur interne s'est produite (Ollama est-il lancé ?). " +
                    "Détails : " + e.getMessage();
            return Flux.just(errorMsg);
        }
    }

    private String extractRoleFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "USER"; // Default to lowest privilege
        }
        try {
            String token = authHeader.substring(7);
            String[] parts = token.split("\\.");
            if (parts.length < 2) return "USER";
            
            String payload = new String(java.util.Base64.getDecoder().decode(parts[1]));
            if (payload.contains("\"roles\":[\"ADMIN\"]") || payload.contains("ADMIN")) {
                return "ADMIN";
            }
        } catch (Exception e) {
            // Ignore parse errors, default to USER
        }
        return "USER";
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
