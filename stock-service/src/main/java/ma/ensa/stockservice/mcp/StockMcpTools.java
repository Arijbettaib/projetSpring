package ma.ensa.stockservice.mcp;

import ma.ensa.stockservice.entity.Stock;
import ma.ensa.stockservice.service.StockService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StockMcpTools {

    private final StockService service;

    public StockMcpTools(StockService service) {
        this.service = service;
    }

    @Tool(name = "get_stock_by_product", description = "Get stock information by product id")
    public Stock getStockByProduct(@ToolParam(description = "The product ID") Long productId) {
        return service.findByProductId(productId);
    }

    @Tool(name = "update_stock_quantity", description = "Update stock quantity for a product")
    public Stock updateStockQuantity(
            @ToolParam(description = "The product ID") Long productId,
            @ToolParam(description = "The new quantity") Integer newQuantity) {
        Stock stock = service.findByProductId(productId);
        stock.setQuantity(newQuantity);
        return service.save(stock);
    }

    @Tool(name = "check_stock_availability", description = "Check if sufficient stock is available for requested quantity")
    public Map<String, Object> checkStockAvailability(
            @ToolParam(description = "The product ID") Long productId,
            @ToolParam(description = "The requested quantity") Integer requestedQuantity) {
        Stock stock = service.findByProductId(productId);
        boolean available = stock.getQuantity() >= requestedQuantity;
        return Map.of(
                "productId", productId,
                "available", available,
                "currentQuantity", stock.getQuantity(),
                "requestedQuantity", requestedQuantity
        );
    }

    @Tool(name = "decrease_stock", description = "Decrease stock quantity (for sales/consumption)")
    public String decreaseStock(
            @ToolParam(description = "The product ID") Long productId,
            @ToolParam(description = "The quantity to decrease") Integer quantity) {
        Stock stock = service.findByProductId(productId);
        if (stock.getQuantity() < quantity) {
            return "ERROR: Insufficient stock. Have " + stock.getQuantity() + 
                   " but " + quantity + " requested";
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        service.save(stock);
        return "Stock decreased by " + quantity + ". Remaining: " + stock.getQuantity();
    }

    @Tool(name = "get_all_stocks", description = "Get all stocks in the system")
    public List<Stock> getAllStocks() {
        return service.findAll();
    }

    @Tool(name = "create_stock_record", description = "Create a new stock record for a product")
    public Stock createStockRecord(
            @ToolParam(description = "The product ID") Long productId,
            @ToolParam(description = "The initial quantity") Integer initialQuantity) {
        return service.create(productId, initialQuantity);
    }
}