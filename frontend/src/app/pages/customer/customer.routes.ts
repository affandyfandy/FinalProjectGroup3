import { Routes } from "@angular/router";
import { RouterConfig } from "../../config/route.constants";

export const CustomerRoutes: Routes = [
    {
        path: RouterConfig.ROOM.path,
        loadChildren: () =>
            import('./room/room.routes')
                .then(m => m.RoomRoutes)
    },
    {
        path: RouterConfig.USER.path,
        loadChildren: () =>
            import('./user/user.routes')
                .then(m => m.UserRoutes)
    }
];