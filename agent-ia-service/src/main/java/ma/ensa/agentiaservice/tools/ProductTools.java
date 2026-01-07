package ma.ensa.agentiaservice.tools;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Product tools placeholder for MCP client integration.
 * The actual tools are automatically discovered from MCP servers (product-service, stock-service)
 * via the ChatClient configuration in ChatClientConfiguration.
 */
@Component
public class ProductTools {

    private static final Logger logger = LoggerFactory.getLogger(ProductTools.class);

    public ProductTools() {
        logger.info("ProductTools component initialized - MCP tools will be auto-discovered");
    }
}
