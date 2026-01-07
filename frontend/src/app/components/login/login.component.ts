import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  credentials = {
    username: '',
    password: ''
  };

  error: string | null = null;
  loading = false;

  onSubmit(): void {
    if (!this.credentials.username || !this.credentials.password) return;

    this.loading = true;
    this.error = null;

    this.authService.login(this.credentials).subscribe({
      next: () => {
        this.router.navigate(['/chat']);
      },
      error: (err) => {
        this.error = 'Invalid credentials or server error';
        this.loading = false;
        console.error(err);
      },
      complete: () => {
        this.loading = false;
      }
    });
  }
}
