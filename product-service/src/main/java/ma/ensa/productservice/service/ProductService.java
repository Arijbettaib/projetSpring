package ma.ensa.productservice.service;

import ma.ensa.productservice.entity.Product;
import ma.ensa.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Product save(Product p) {
        return repo.save(p);
    }

    public Product create(Product product) {
        return repo.save(product);
    }

    public Product findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public long count() {
        return repo.count();
    }

    public List<Product> findByPriceRange(double minPrice, double maxPrice) {
        return repo.findAll().stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public Product updatePrice(Long productId, double newPrice) {
        Product product = findById(productId);
        product.setPrice(newPrice);
        return repo.save(product);
    }
}