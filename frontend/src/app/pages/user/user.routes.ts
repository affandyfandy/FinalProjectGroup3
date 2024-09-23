import { Routes } from "@angular/router";
import { UserListComponent } from "../admin/user-list/user-list.component";
import { ProfileComponent } from "./profile/profile.component";
import { HomepageComponent } from "./homepage/homepage.component";
import { RoomListComponent } from "./room-list/room-list.component";
import { RoomFormComponent } from "./room-form/room-form.component";

export const UserRoutes: Routes = [
    { path: '', component: HomepageComponent},
    // { path: '', component: UserListComponent },
    // { path: ':id', component: ProfileComponent },
    { path: 'rooms', component: RoomListComponent},
    { path: 'rooms/:id', component: RoomFormComponent},
    // { path: 'book/:id', component: ReservationListComponent}
];