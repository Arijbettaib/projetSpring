import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const adminGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    // Decode the token to check roles (simple checking logic)
    // In a real app we would decode the JWT properly.
    // Here we'll rely on a method from AuthService if available, or decode simply.

    const token = authService.getToken();
    if (!token) {
        router.navigate(['/login']);
        return false;
    }

    // Basic JWT decoding for role check
    try {
        const payloadStart = token.indexOf('.') + 1;
        const payloadEnd = token.lastIndexOf('.');
        const payload = token.substring(payloadStart, payloadEnd);
        const decoded = JSON.parse(atob(payload));

        const roles: string[] = decoded.roles || [];
        if (roles.includes('ADMIN')) {
            return true;
        }
    } catch (e) {
        console.error("Token decoding failed", e);
    }

    router.navigate(['/chat']); // Redirect non-admins to chat
    return false;
};
