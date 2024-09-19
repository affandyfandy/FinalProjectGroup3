import { Routes } from '@angular/router';
import { RouterConfig } from './config/route.constants';
import { AuthGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    {
        path: RouterConfig.AUTH.path,
        canActivate: [AuthGuard],
        loadChildren: () =>
            import('./pages/auth/auth.routes')
                .then(m => m.AuthRoutes)
    }
];
