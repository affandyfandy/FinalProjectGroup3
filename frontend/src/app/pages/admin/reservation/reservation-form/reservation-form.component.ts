import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Reservation } from '../../../../model/reservation.model';
import { ReservationService } from '../../../../services/reservation/reservation.service';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroCalendarDays, heroChevronLeft, heroCreditCard, heroMapPin } from '@ng-icons/heroicons/outline';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { heroStarSolid, heroUserSolid } from '@ng-icons/heroicons/solid';

@Component({
  selector: 'app-reservation-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent,
    RouterModule
  ],
  templateUrl: './reservation-form.component.html',
  providers: [
    provideIcons({ heroMapPin, heroChevronLeft, heroStarSolid, heroCalendarDays, heroUserSolid, heroCreditCard})
  ]
})
export class ReservationFormComponent implements OnInit {
  @Input() reservation: Reservation | null = null;
  @Input() action?: string;
  @Output() save = new EventEmitter<Reservation>();
  @Output() cancel = new EventEmitter<void>();

  reservationId?: string | null = null;

  reservationForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private reservationService: ReservationService,
    private route: ActivatedRoute,
    private router: Router) {
    this.reservationForm = this.fb.group({
      customerName: ['', Validators.required],
      roomType: ['', Validators.required],
      checkInDate: ['', Validators.required],
      checkOutDate: ['', Validators.required],
      status: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    const currentRoute = this.route.snapshot.url[this.route.snapshot.url.length - 1]?.path;
    this.action = currentRoute === 'edit' ? 'Edit Reservation' : (currentRoute === 'create' ? 'Add New Reservation' : 'Reservation Details');
    this.reservationId = this.route.snapshot.paramMap.get('id');
    this.loadReservation();
  }

  loadReservation(): void {
    if (this.reservationId) {
      this.reservationService.getReservationById(this.reservationId).subscribe({
        next: (reservation: Reservation) => {
          this.reservation = reservation;
          this.reservationForm.patchValue({
            customerName: this.reservation.userId,
            roomId: this.reservation.roomId,
            checkInDate: this.reservation.checkInDate,
            checkOutDate: this.reservation.checkOutDate,
            status: this.reservation.status
          });
        },
        error: (err) => {
          console.error('Error fetching reservation:', err);
        }
      });
    }
  }

  onSubmit(): void {
    if (this.reservationForm.valid) {
      const reservationData = { ...this.reservationForm.value };

      if (this.action === 'Add New Reservation') {
        this.reservationService.createReservation(reservationData).subscribe({
          next: (createdReservation) => {
            this.save.emit(createdReservation);
          },
          error: (err) => {
            console.error('Error creating reservation:', err);
          }
        });
      }
    }
  }

  onClose(): void {
    this.router.navigate(['/admin/reservations']);
    this.cancel.emit();
  }
}
