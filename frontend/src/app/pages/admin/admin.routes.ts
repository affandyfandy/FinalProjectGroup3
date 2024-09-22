import { Routes } from '@angular/router';
import { RoomFormComponent } from './room-form/room-form.component';
import { RoomListComponent } from './room-list/room-list.component';

export const AdminRoutes: Routes = [
  { path: 'rooms', component: RoomListComponent },
  { path: 'rooms/create', component: RoomFormComponent },
  { path: 'rooms/edit/:id', component: RoomFormComponent }
];