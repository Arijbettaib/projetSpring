package ma.ensa.stockservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "PRODUCT-SERVICE",
        fallback = ProductServiceFallback.class
)
public interface ProductServiceClient {

    @GetMapping("/api/products/{id}")
    Object getProductById(@PathVariable("id") Long id);

    @GetMapping("/api/products")
    Object getAllProducts();
}
