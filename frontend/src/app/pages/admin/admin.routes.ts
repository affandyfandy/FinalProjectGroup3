import { Routes } from '@angular/router';
import { RoomFormComponent } from './room/room-form/room-form.component';
import { RoomListComponent } from './room/room-list/room-list.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RouterConfig } from '../../config/route.constants';

export const AdminRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
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
  },
  {
    path: RouterConfig.RESERVATION.path,
    loadChildren: () =>
      import('./reservation/reservation.routes')
        .then(m => m.ReservationRoutes)
  }
];
