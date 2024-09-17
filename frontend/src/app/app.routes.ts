import { Routes } from '@angular/router';
import { RouterConfig } from './config/route.constants';

export const routes: Routes = [
    {
        path: RouterConfig.AUTH.path,
        loadChildren: () =>
            import('./pages/auth/auth.routes')
                .then(m => m.AuthRoutes)
    }
];
