package ma.ensa.agentiaservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String message;
    private boolean success;
    private long timestamp;

    public ChatResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.timestamp = System.currentTimeMillis();
    }
}
