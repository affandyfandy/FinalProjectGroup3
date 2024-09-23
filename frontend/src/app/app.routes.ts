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
        data: { roles: ['CUSTOMER'] },
        loadChildren: () =>
            import('./pages/user/user.routes')
                .then(m => m.UserRoutes)
    },
    {
        path: RouterConfig.ADMIN.path,
        // canActivate: [AuthGuard],
        // data: { roles: ['ADMIN'] },
        loadChildren: () =>
            import('./pages/admin/admin.routes')
                .then(m => m.AdminRoutes)
    },
    // {
    //     path: 'user', 
    //     canActivate: [AuthGuard],
    //     loadChildren: () =>
    //         import('./pages/user/user.routes')
    //             .then(m => m.UserRoutes)
    // },
    {
        path: 'profile',
        loadComponent: () => 
            import('./pages/user/profile/profile.component').then(m => m.ProfileComponent)
    }
];
