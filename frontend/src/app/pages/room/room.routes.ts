import { Routes } from '@angular/router';
import { RoomListComponent } from './room-list/room-list.component';
import { RoomFormComponent } from './room-form/room-form.component';

export const RoomRoutes: Routes = [
  { path: '', component: RoomListComponent },
  { path: 'create', component: RoomFormComponent }
];