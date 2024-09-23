import { Routes } from '@angular/router';
import { RoomFormComponent } from './room-form/room-form.component';
import { RoomListComponent } from './room-list/room-list.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserListComponent } from './user-list/user-list.component';

export const AdminRoutes: Routes = [
  { path: 'rooms', component: RoomListComponent },
  { path: 'rooms/create', component: RoomFormComponent },
  { path: 'rooms/:id/edit', component: RoomFormComponent },
  { path: 'rooms/:id', component: RoomFormComponent },
  { path: 'users', component: UserListComponent },
  { path: '', component: DashboardComponent} 
];
