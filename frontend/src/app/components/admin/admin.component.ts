import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService, Product } from '../../services/product.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-admin',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './admin.component.html',
    styles: [`
    .container { max-width: 900px; margin: 2rem auto; padding: 1rem; }
    .card { background: white; padding: 2rem; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); margin-bottom: 2rem; }
    h2 { color: #2c3e50; margin-bottom: 1.5rem; }
    table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
    th, td { padding: 1rem; text-align: left; border-bottom: 1px solid #eee; }
    th { background-color: #f8f9fa; font-weight: 600; }
    .btn-delete { background: #e74c3c; color: white; padding: 0.5rem 1rem; border: none; border-radius: 6px; cursor: pointer; }
    .btn-add { background: #2ecc71; color: white; padding: 0.8rem 2rem; border: none; border-radius: 6px; cursor: pointer; font-size: 1rem; }
    .form-group { margin-bottom: 1rem; }
    label { display: block; margin-bottom: 0.5rem; color: #666; }
    input { width: 100%; padding: 0.8rem; border: 1px solid #ddd; border-radius: 6px; font-size: 1rem; }
    .navbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; }
  `]
})
export class AdminComponent implements OnInit {
    private productService = inject(ProductService);
    private router = inject(Router);

    products: Product[] = [];
    newProduct: Product = { name: '', price: 0, quantity: 0 };

    ngOnInit(): void {
        this.loadProducts();
    }

    loadProducts(): void {
        this.productService.getProducts().subscribe({
            next: (data) => this.products = data,
            error: (err) => console.error('Failed to load products', err)
        });
    }

    addProduct(): void {
        this.productService.createProduct(this.newProduct).subscribe({
            next: (product) => {
                this.products.push(product);
                this.newProduct = { name: '', price: 0, quantity: 0 }; // Reset form
            },
            error: (err) => alert('Error creating product: ' + err.message)
        });
    }

    deleteProduct(id: number | undefined): void {
        if (!id) return;
        if (confirm('Are you sure you want to delete this product?')) {
            this.productService.deleteProduct(id).subscribe({
                next: () => {
                    this.products = this.products.filter(p => p.id !== id);
                },
                error: (err) => alert('Error deleting product')
            });
        }
    }

    goBack(): void {
        this.router.navigate(['/chat']);
    }
}
