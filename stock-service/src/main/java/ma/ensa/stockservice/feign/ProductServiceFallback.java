package ma.ensa.stockservice.feign;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * Fallback implementation for Product Service Client
 * Called when Product Service is unavailable
 */
@Component
public class ProductServiceFallback implements ProductServiceClient {

    @Override
    public Object getProductById(Long id) {
        return Map.of(
                "error", "Product Service is currently unavailable",
                "productId", id,
                "fallback", true
        );
    }

    @Override
    public Object getAllProducts() {
        return Map.of(
                "error", "Product Service is currently unavailable",
                "fallback", true,
                "products", Collections.emptyList()
        );
    }
}
