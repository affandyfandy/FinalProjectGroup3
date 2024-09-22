import { Routes } from "@angular/router";
import { UserListComponent } from "./user-list/user-list.component";
import { ProfileComponent } from "./profile/profile.component";

export const UserRoutes: Routes = [
    { path: '', component: UserListComponent },
    { path: ':id', component: ProfileComponent }
];