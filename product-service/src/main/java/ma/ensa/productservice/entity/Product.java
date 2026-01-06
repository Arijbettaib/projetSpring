package ma.ensa.productservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private int quantity;

    // 🔹 Constructeur vide (OBLIGATOIRE JPA)
    public Product() {}

    // 🔹 Constructeur utilisé par MCP
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 0;
    }

    // 🔹 Getters & setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}