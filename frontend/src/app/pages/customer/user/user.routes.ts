import { Routes } from "@angular/router";

export const UserRoutes: Routes = [
    {
        path: 'profile',
        loadComponent: () => import('./profile/profile.component').then(m => m.ProfileComponent)
    }
];