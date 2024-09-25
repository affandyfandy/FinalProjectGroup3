import { Routes } from '@angular/router';
import { RouterConfig } from './config/route.constants';
import { AuthGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    {
        path: RouterConfig.AUTH.path,
        loadChildren: () =>
            import('./pages/auth/auth.routes')
                .then(m => m.AuthRoutes)
    },
    {
        path: RouterConfig.CUSTOMER.path,
        loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent),
        canActivate: [AuthGuard]
    },
    {
        path: RouterConfig.ADMIN.path,
        loadChildren: () =>
            import('./pages/admin/admin.routes')
                .then(m => m.AdminRoutes),
        canActivate: [AuthGuard]
    },
    {
        path: RouterConfig.CUSTOMER.path,
        loadChildren: () =>
            import('./pages/customer/customer.routes')
                .then(m => m.CustomerRoutes),
        canActivate: [AuthGuard]
    },
    {
        path: RouterConfig.UNAUTHORIZED.path,
        loadComponent: () => import('./pages/unauthorized/unauthorized.component').then(m => m.UnauthorizedComponent),
    },
    {
        path: RouterConfig.NOT_FOUND.path,
        loadComponent: () => import('./pages/not-found/not-found.component').then(m => m.NotFoundComponent),
    },
];
