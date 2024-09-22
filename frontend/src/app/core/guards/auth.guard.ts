import { CanActivateFn, Router } from "@angular/router";
import { AuthService } from "../../services/auth/auth.service";
import { inject } from "@angular/core";
import { RouterConfig } from "../../config/route.constants";

export const AuthGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (authService.checkCredentials()) {
        router.navigate(['/']);
        return false;
    }
    else {
        router.navigate(['/unauthorized']);
        return false;
    }

    return true;
}