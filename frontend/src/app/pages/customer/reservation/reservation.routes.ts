import { Routes } from "@angular/router";

export const ReservationRoutes: Routes = [
    {
        path: 'create',
        loadComponent: () => import('./reservation-form/reservation-form.component').then(m => m.ReservationFormComponent)
    },
];