import { CanActivateFn, Router } from "@angular/router";
import { AuthService } from "../../services/auth/auth.service";
import { inject } from "@angular/core";
import { RouterConfig } from "../../config/route.constants";

export const AuthGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const isLoggedIn = authService.checkCredentials();
    const isAdmin = authService.isAdmin();
    const url = state.url;


    if (!isLoggedIn) {
        if (url === '/reservation') {
            router.navigate(['/auth/login']);
            return false;
        }

        if (url.includes(RouterConfig.ADMIN.path)) {
            router.navigate(['/auth/login']);
            return false;
        }

        if (url === '/'  || url.includes(RouterConfig.ROOM.path) || url.includes('/reservations/unavailable-dates')) {
            return true;
        } else {
            router.navigate(['/auth/login']);
            return false;
        }

        
    }

    if (isLoggedIn) {
        if (url.includes(RouterConfig.AUTH.path)) {
            router.navigate([RouterConfig.CUSTOMER.path]);
            return false;
        }
        if (isAdmin) {
            if (url == '/') {
                router.navigate([RouterConfig.ADMIN.path, RouterConfig.ROOM.path]);
                return false;
            }
            if (!url.includes(RouterConfig.ADMIN.path) && !url.includes('profile')) {
                router.navigate([RouterConfig.UNAUTHORIZED.path]);
                return false;
            }
        }
        if (!isAdmin) {
            if (url.includes(RouterConfig.ADMIN.path)) {
                router.navigate([RouterConfig.UNAUTHORIZED.path]);
                return false;
            }
        }

        return true;
    }
    
    return true;
}