package ma.ensa.agentiaservice.model;

import java.util.Map;

public class McpRequest {
    public String jsonrpc = "2.0";
    public String id = "1";
    public String method;
    public Map<String, Object> params;
}