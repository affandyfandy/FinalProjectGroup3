import { Routes } from '@angular/router';
import { RouterConfig } from './config/route.constants';

export const routes: Routes = [
    {
        path: RouterConfig.AUTH.path,
        loadChildren: () =>
            import('./pages/auth/auth.routes')
                .then(m => m.AuthRoutes)
    },
    {
        path: RouterConfig.ROOM.path,
        loadChildren: () =>
            import('./pages/room/room.routes')
                .then(m => m.RoomRoutes)
    }
];
