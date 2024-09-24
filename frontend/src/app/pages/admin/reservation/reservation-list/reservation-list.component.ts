import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReservationService } from '../../../../services/reservation/reservation.service';
import { Reservation } from '../../../../model/reservation.model';


import { NgIf, NgFor, CurrencyPipe, DatePipe } from '@angular/common';
import { RoomService } from '../../../../services/room.service';
import { Room } from '../../../../model/room.model';

@Component({
  selector: 'app-reservation-list',
  standalone: true,
  imports: [CommonModule, NgIf, NgFor, CurrencyPipe, DatePipe],
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

  constructor(private reservationService: ReservationService, private router: Router, private roomService: RoomService) {}

  ngOnInit(): void {
    this.loadReservations(this.currentPage);
    this.reservationService.getAllReservationsWithRooms().subscribe(reservations => {
      console.log(reservations);
    });
    console.log(this.reservations[0].room);
    
    this.reservations.forEach(reservation => {
      if (reservation && reservation.room && reservation.room.roomNumber) {
        console.log(reservation.room.roomNumber);
      } else {
        console.log('Room information is missing for this reservation.');
      }
    });
    
    
  }
  

  loadReservations(page: number): void {
    this.reservationService.getAllReservationsWithRooms(page - 1, this.pageSize).subscribe({
      next: (response: any) => {
        this.reservations = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
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
    this.router.navigate(['/admin/reservation/create']);
  }

  changePage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.loadReservations(this.currentPage);
  }

  openDetailsModal(reservation: Reservation): void {
    this.selectedReservation = reservation;
  }

  closeDetailsModal(): void {
    this.selectedReservation = null;
  }

  get startIndex(): number {
    return (this.currentPage - 1) * this.pageSize + 1;
  }

  get endIndex(): number {
    return Math.min(this.currentPage * this.pageSize, this.totalElements);
  }
}
