import { Routes } from '@angular/router';
import { RoomFormComponent } from './room-form/room-form.component';
import { RoomListComponent } from './room-list/room-list.component';
import { AdminGuard } from '../../core/guards/admin.guard';

export const RoomRoutes: Routes = [
  { path: '', component: RoomListComponent }, // Default route for the room list
  { path: 'create', component: RoomFormComponent, canActivate: [AdminGuard] }, // Protected route
  { path: 'edit/:id', component: RoomFormComponent, canActivate: [AdminGuard] } // Protected route for editing
];