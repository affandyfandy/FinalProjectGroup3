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
        path: ':id',
        loadComponent: () => import('./room-form/room-form.component').then(m => m.RoomFormComponent)
    },
    {
        path: 'reservation/:id',
        loadComponent: () => import('./reservation-form/reservation-form.component').then(m => m.ReservationFormComponent)
    },
    {
        path: 'payment/:id',
        loadComponent: () => import('./payment/payment.component').then(m => m.PaymentFormComponent)
    }

];
