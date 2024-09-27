import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Room } from '../../../../model/room.model';
import { UserService } from '../../../../services/user.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { RoomService } from '../../../../services/room.service';
import { User } from '../../../../model/user.model';
import { CommonModule } from '@angular/common';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft, heroEllipsisVertical, heroExclamationTriangle, heroInformationCircle, heroStar } from '@ng-icons/heroicons/outline';
import { heroUserSolid, heroStarSolid, heroAdjustmentsHorizontalSolid, heroInformationCircleSolid, heroHomeSolid, heroCreditCardSolid, heroUserCircleSolid } from '@ng-icons/heroicons/solid';
import { ToastService } from '../../../../services/toast.service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ReservationService } from '../../../../services/reservation/reservation.service';
import { Payment } from '../../../../model/payment.model';
import { PaymentService } from '../../../../services/reservation/payment.service';

@Component({
  selector: 'app-reservation-form',
  standalone: true,
  imports: [
    CommonModule,
    NgIconComponent,
    ReactiveFormsModule
  ],
  templateUrl: './reservation-form.component.html',
  styleUrl: './reservation-form.component.scss',
  providers: [
    provideIcons({ heroEllipsisVertical, heroChevronLeft, heroUserSolid, heroUserCircleSolid, heroStarSolid, heroExclamationTriangle, heroHomeSolid, heroCreditCardSolid})
  ]
})
export class ReservationFormComponent implements OnInit {

  user: User | null = null;
  room: Room | null = null;

  queryParam: any = {};
  totalDays: number = 0;
  totalCost: number = 0;

  reservationForm: FormGroup | any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    private roomService: RoomService,
    private toastService: ToastService,
    private reservationService: ReservationService,
    private paymentService: PaymentService,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    if (!this.authService.checkCredentials()) {
      this.router.navigate(['/auth/login']);
      this.toastService.showToast('Please login first', 'warning');
    }

    this.activatedRoute.queryParams.subscribe(params => {
      this.queryParam = params;

      const checkInDate = new Date(this.queryParam.checkIn);
      const checkOutDate = new Date(this.queryParam.checkOut);

      if (this.queryParam.roomId === undefined) {
        this.toastService.showToast('Please select a room', 'warning');
        this.router.navigate(['/room']);
      }

      if (this.queryParam.checkIn === undefined || this.queryParam.checkOut === undefined) {
        this.toastService.showToast('Please select check-in and check-out date', 'warning');
        this.router.navigate(['/room']);
      }

      if (checkInDate && checkOutDate) {
        const diffTime = Math.abs(checkOutDate.getTime() - checkInDate.getTime());
        this.totalDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      }


    });
    this.loadData(this.totalDays);

    this.reservationForm = this.fb.group({
      checkInDate: [this.queryParam.checkIn],
      checkOutDate: [this.queryParam.checkOut],
      roomId: [this.queryParam.roomId],
      amount: [this.totalCost],
      status: ['CONFIRMED'],
      createdTime: [new Date().toDateString()],
      userId: [this.authService.getUserInformation()[1].value],
      accountNumber: [''],
      accountName: [''],
    });

  }

  loadData(totalDays: number = 0): void {
    if (this.authService.checkCredentials()) {
      this.userService.getUserById(this.authService.getUserInformation()[1].value).subscribe((user: User) => {
        this.user = user;
      });
      this.getTotalDays();
      
      
      this.roomService.getRoomById(this.queryParam.roomId).subscribe((room: Room) => {
        this.totalCost = totalDays * room?.price;
        this.totalDays = totalDays;
        this.room = room;
      });
    }
  }

  confirmReservation() {
    if (this.reservationForm.valid) {
      console.log('Reservation Form:', this.reservationForm.value);
      this.reservationService.createReservation(this.reservationForm.value).subscribe({
        next: (reservation) => {
          console.log('Reservation created:', reservation);
          this.toastService.showToast('Reservation created successfully!', 'success');

          const payment: Payment = {
            accountName: this.reservationForm.value.accountName,
            accountNumber: this.reservationForm.value.accountNumber,
            amount: this.totalCost,
            reservationId: reservation.id,
          }

          this.paymentService.createPayment(payment, reservation.id).subscribe({
            next: (payment) => {
              console.log('Payment created:', payment);
              this.toastService.showToast('Payment created successfully!', 'success');
              this.router.navigate(['/reservation']);
            },
            error: (err) => {
              console.error('Error creating payment:', err);
              this.toastService.showToast('Error creating payment: ' + err, 'error');
            }
          });
        },
        error: (err) => {
          console.error('Error creating reservation:', err);
          this.toastService.showToast('Error creating reservation: ' + err, 'error');
        }
      });
    } else {
      this.toastService.showToast('Please fill all required fields', 'warning');
    }
  }

  getTotalDays(): void{
    this.totalDays = this.queryParam.checkIn - this.queryParam.checkOut;
  }
}
