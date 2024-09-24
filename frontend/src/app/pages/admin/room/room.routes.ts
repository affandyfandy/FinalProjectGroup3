import { Routes } from "@angular/router";

export const RoomRoutes: Routes = [
    {
        path: '',
        loadComponent: () => import('./room-list/room-list.component').then(m => m.RoomListComponent)
    },
    {
        path: 'create',
        loadComponent: () => import('./room-form/room-form.component').then(m => m.RoomFormComponent)
    },
    {
        path: ':id/edit',
        loadComponent: () => import('./room-form/room-form.component').then(m => m.RoomFormComponent)
    },
    {
        path: ':id/view',
        loadComponent: () => import('./room-form/room-form.component').then(m => m.RoomFormComponent)
    },
    {
        path: 'import',
        loadComponent: () => import('./room-form/room-form.component').then(m => m.RoomFormComponent)
    }
];