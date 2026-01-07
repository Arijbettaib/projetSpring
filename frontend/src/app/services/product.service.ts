import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface Product {
    id?: number;
    name: string;
    price: number;
    quantity: number;
}

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    private apiUrl = 'http://localhost:8888/api/products';
    private http = inject(HttpClient);
    private authService = inject(AuthService);

    getProducts(): Observable<Product[]> {
        const headers = this.getHeaders();
        return this.http.get<Product[]>(this.apiUrl, { headers });
    }

    createProduct(product: Product): Observable<Product> {
        const headers = this.getHeaders();
        return this.http.post<Product>(this.apiUrl, product, { headers });
    }

    deleteProduct(id: number): Observable<void> {
        const headers = this.getHeaders();
        return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers });
    }

    private getHeaders(): HttpHeaders {
        return new HttpHeaders({
            'Authorization': `Bearer ${this.authService.getToken()}`,
            'Content-Type': 'application/json'
        });
    }
}
