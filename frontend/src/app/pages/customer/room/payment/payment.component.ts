import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../../../../services/reservation/payment.service';
import { ReservationService } from '../../../../services/reservation/reservation.service';
import { Payment } from '../../../../model/payment.model';
import { Reservation } from '../../../../model/reservation.model';

@Component({
  selector: 'app-payment-form',
  templateUrl: './payment.component.html'
})
export class PaymentFormComponent implements OnInit {
  payment: Payment = {
    id: '',
    accountNumber: '',
    accountName: '',
    amount: 0,
    reservationId: ''
  };

  constructor(
    private route: ActivatedRoute,
    private paymentService: PaymentService,
    private reservationService: ReservationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const reservationId = this.route.snapshot.paramMap.get('id');
    if (reservationId) {
      this.payment.reservationId = reservationId;
    }
  }

  submitPayment(): void {
    this.paymentService.createPayment(this.payment, this.payment.reservationId).subscribe(() => {
      this.updateReservationStatus();
    });
  }

  updateReservationStatus(): void {
    this.reservationService.getReservationById(this.payment.reservationId).subscribe((reservation: Reservation) => {
      reservation.status = 'Confirmed';
      this.reservationService.updateReservation(reservation.id, reservation).subscribe(() => {
        this.router.navigate(['/rooms']);
      });
    });
  }
}
