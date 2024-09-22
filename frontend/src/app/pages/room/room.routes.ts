import { Routes } from '@angular/router';
import { RoomListComponent } from './customer/room-list/room-list.component';
import { RoomFormComponent } from './admin/room-form/room-form.component';

export const RoomRoutes: Routes = [
  { path: '', component: CustomerRoomListComponent },
  { path: '', component: CustomerRoomListComponent },
  { path: 'create', component: RoomFormComponent }
];