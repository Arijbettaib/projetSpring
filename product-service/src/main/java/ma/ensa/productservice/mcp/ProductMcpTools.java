package ma.ensa.productservice.mcp;

import ma.ensa.productservice.entity.Product;
import ma.ensa.productservice.service.ProductService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductMcpTools {

    private final ProductService service;

    public ProductMcpTools(ProductService service) {
        this.service = service;
    }

    @Tool(name = "create_product", description = "Create a new product with name and price. Returns the created product with ID.")
    public Product createProduct(
            @ToolParam(description = "The name of the product") String name,
            @ToolParam(description = "The price of the product") double price) {
        return service.create(new Product(name, price));
    }

    @Tool(name = "get_all_products", description = "Retrieve all products available in the system")
    public List<Product> getAllProducts() {
        return service.findAll();
    }

    @Tool(name = "get_product_by_id", description = "Retrieve a specific product by its ID")
    public Product getProductById(@ToolParam(description = "The product ID") Long id) {
        return service.findById(id);
    }

    @Tool(name = "search_products_by_price", description = "Find products within a price range (min to max inclusive)")
    public List<Product> searchByPriceRange(
            @ToolParam(description = "Minimum price") double minPrice,
            @ToolParam(description = "Maximum price") double maxPrice) {
        return service.findByPriceRange(minPrice, maxPrice);
    }

    @Tool(name = "update_product_price", description = "Update the price of a product by its ID")
    public Product updatePrice(
            @ToolParam(description = "The product ID") Long productId,
            @ToolParam(description = "The new price") double newPrice) {
        return service.updatePrice(productId, newPrice);
    }

    @Tool(name = "get_product_info", description = "Get detailed information about a product")
    public Map<String, Object> getProductInfo(@ToolParam(description = "The product ID") Long productId) {
        Product product = service.findById(productId);
        return Map.of(
            "id", product.getId(),
            "name", product.getName(),
            "price", product.getPrice(),
            "quantity", product.getQuantity()
        );
    }

    @Tool(name = "delete_product", description = "Delete a product by its ID")
    public String deleteProduct(@ToolParam(description = "The product ID") Long productId) {
        try {
            service.delete(productId);
            return "Product " + productId + " deleted successfully";
        } catch (Exception e) {
            return "Error deleting product: " + e.getMessage();
        }
    }

    @Tool(name = "count_products", description = "Get the total number of products in the system")
    public Map<String, Object> countProducts() {
        long count = service.count();
        return Map.of("totalProducts", count);
    }
}