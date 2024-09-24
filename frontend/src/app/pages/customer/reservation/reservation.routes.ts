import { Routes } from "@angular/router";

export const ReservationRoutes: Routes = [
    {
        path: '',
        loadComponent: () => import('./reservation-list/reservation-list.component').then(m => m.ReservationListComponent)
    },
    {
        path: 'reschedule',
        loadComponent: () => import('./reschedule-form/reschedule-form.component').then(m => m.RescheduleFormComponent)
    }
];
