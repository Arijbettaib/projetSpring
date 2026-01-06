package ma.ensa.stockservice.controller;

import ma.ensa.stockservice.entity.Stock;
import ma.ensa.stockservice.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Stock> getByProductId(@PathVariable Long productId) {
        try {
            Stock stock = service.findByProductId(productId);
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Stock> create(@RequestBody Map<String, Object> body) {
        try {
            Long productId = Long.parseLong(body.get("productId").toString());
            Integer quantity = Integer.parseInt(body.get("quantity").toString());
            Stock stock = service.create(productId, quantity);
            return ResponseEntity.status(HttpStatus.CREATED).body(stock);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Stock> update(@PathVariable Long productId, @RequestBody Map<String, Object> body) {
        try {
            Integer newQuantity = Integer.parseInt(body.get("quantity").toString());
            Stock stock = service.updateQuantity(productId, newQuantity);
            return ResponseEntity.ok(stock);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        try {
            service.delete(productId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{productId}/decrease")
    public ResponseEntity<Map<String, Object>> decreaseStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        try {
            Stock stock = service.findByProductId(productId);
            if (stock.getQuantity() < quantity) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Insufficient stock. Have " + stock.getQuantity() + " but " + quantity + " requested"
                ));
            }
            stock.setQuantity(stock.getQuantity() - quantity);
            service.save(stock);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Stock decreased by " + quantity,
                "remaining", stock.getQuantity()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/{productId}/check")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @PathVariable Long productId,
            @RequestParam Integer requestedQuantity) {
        try {
            Stock stock = service.findByProductId(productId);
            boolean available = stock.getQuantity() >= requestedQuantity;
            return ResponseEntity.ok(Map.of(
                "available", available,
                "currentQuantity", stock.getQuantity(),
                "requestedQuantity", requestedQuantity
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "available", false,
                "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Stock Service is healthy");
    }
}