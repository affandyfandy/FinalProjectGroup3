import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';  // Import tambahan untuk form
import { ReservationService } from '../../../../services/reservation/reservation.service';
import { Reservation } from '../../../../model/reservation.model';

import { NgIf, NgFor, CurrencyPipe, DatePipe } from '@angular/common';
import { RoomService } from '../../../../services/room.service';
import { Room } from '../../../../model/room.model';

@Component({
  selector: 'app-reservation-list',
  standalone: true,
  imports: [CommonModule, NgIf, NgFor, CurrencyPipe, DatePipe, ReactiveFormsModule],
  templateUrl: './reservation-list.component.html',
})
export class ReservationListComponent implements OnInit {
  reservations: Reservation[] = [];
  roomMap: Map<string, string> = new Map();
  totalElements: number = 0;
  totalPages: number = 0;
  currentPage: number = 1;
  pageSize: number = 10;
  error: string = '';
  selectedReservation: Reservation | null = null;
  rescheduleForm!: FormGroup;

  constructor(
    private reservationService: ReservationService,
    private router: Router,
    private roomService: RoomService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadReservations(this.currentPage);
  }

  loadReservations(page: number): void {
    this.reservationService.getAllReservationsWithRooms(page - 1, this.pageSize).subscribe({
      next: (response: any) => {
        this.reservations = response;
        // this.reservations = response.content;
        // this.totalElements = response.totalElements;
        // this.totalPages = response.totalPages;

        console.log(response);
      },
      error: (err) => {
        this.error = 'Failed to load reservations';
        console.error(err);
      }
    });
  }

  viewReservation(id: string): void {
    this.router.navigate(['/reservations', id]);
  }

  deleteReservation(id: string): void {
    this.reservationService.deleteReservation(id).subscribe({
      next: () => {
        this.loadReservations(this.currentPage);
      },
      error: (err) => {
        this.error = 'Failed to cancel reservation';
        console.error(err);
      }
    });
  }

  createReservation() {
    this.router.navigate(['/rooms']);
  }

  changePage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.loadReservations(this.currentPage);
  }

  openRescheduleModal(reservation: Reservation): void {
    this.selectedReservation = reservation;
    this.rescheduleForm.patchValue({
      checkInDate: reservation.checkInDate,
      checkOutDate: reservation.checkOutDate
    });
  }

  closeRescheduleModal(): void {
    this.selectedReservation = null;
  }

  submitReschedule(): void {
    if (this.rescheduleForm.valid && this.selectedReservation) {
      const updatedReservation = {
        ...this.selectedReservation,
        checkInDate: this.rescheduleForm.value.checkInDate,
        checkOutDate: this.rescheduleForm.value.checkOutDate
      };

    }
  }

  get startIndex(): number {
    return (this.currentPage - 1) * this.pageSize + 1;
  }

  get endIndex(): number {
    return Math.min(this.currentPage * this.pageSize, this.totalElements);
  }
}
