package ma.ensa.agentiaservice.controller;

import ma.ensa.agentiaservice.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    @PostMapping
    public String chat(@RequestBody String message) {
        return service.chat(message);
    }
}