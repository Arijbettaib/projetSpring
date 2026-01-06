package ma.ensa.stockservice.service;

import ma.ensa.stockservice.entity.Stock;
import ma.ensa.stockservice.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepository repository;

    public StockService(StockRepository repository) {
        this.repository = repository;
    }

    public Stock updateStock(Long productId, int quantity) {
        Stock stock = repository.findByProductId(productId)
                .orElseGet(() -> {
                    Stock s = new Stock();
                    s.setProductId(productId);
                    return s;
                });
        stock.setQuantity(quantity);
        return repository.save(stock);
    }

    public Stock findByProductId(Long productId) {
        return repository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found for product: " + productId));
    }

    public Stock save(Stock stock) {
        return repository.save(stock);
    }

    public Stock create(Stock stock) {
        return repository.save(stock);
    }

    public Stock updateQuantity(Long productId, Integer newQuantity) {
        Stock stock = findByProductId(productId);
        stock.setQuantity(newQuantity);
        return repository.save(stock);
    }

    public List<Stock> findAll() {
        return repository.findAll();
    }

    public void delete(Long productId) {
        Stock stock = findByProductId(productId);
        repository.delete(stock);
    }

    public Stock create(Long productId, Integer quantity) {
        Stock stock = new Stock();
        stock.setProductId(productId);
        stock.setQuantity(quantity);
        return repository.save(stock);
    }
}