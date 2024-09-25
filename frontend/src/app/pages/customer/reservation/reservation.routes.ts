import { Routes } from "@angular/router";

export const ReservationRoutes: Routes = [
      {
        path: '',
        loadComponent: () => import('./reservation-list/reservation-list.component').then(m => m.ReservationListComponent)
    },{
        path: 'create',
        loadComponent: () => import('./reservation-form/reservation-form.component').then(m => m.ReservationFormComponent)
    },
    {
        path: 'payment',
        loadComponent: () => import('./payment/payment.component').then(m => m.PaymentComponent)
  },
];
