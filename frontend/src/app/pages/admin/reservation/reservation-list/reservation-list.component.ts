import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReservationService } from '../../../../services/reservation/reservation.service';
import { Reservation } from '../../../../model/reservation.model';


import { NgIf, NgFor, CurrencyPipe, DatePipe } from '@angular/common';
import { RoomService } from '../../../../services/room.service';
import { Room } from '../../../../model/room.model';
import { ToastService } from '../../../../services/toast.service';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroEllipsisVertical, heroChevronUpDown } from '@ng-icons/heroicons/outline';
import { heroUserSolid, heroStarSolid, heroAdjustmentsHorizontalSolid } from '@ng-icons/heroicons/solid';

@Component({
  selector: 'app-reservation-list',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe,NgIconComponent],
  templateUrl: './reservation-list.component.html',
  providers: [
    provideIcons({
      heroEllipsisVertical,
      heroUserSolid,
      heroStarSolid,
      heroAdjustmentsHorizontalSolid,
      heroChevronUpDown})
  ]
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
  showOptions: boolean = false; 
  showDeleteModal: boolean = false;

  constructor(
    private reservationService: ReservationService, 
    private router: Router, 
    private roomService: RoomService,
    private toastService: ToastService) {}

  ngOnInit(): void {
    this.loadReservations(this.currentPage);
  }  

  loadReservations(page: number): void {
    this.reservationService.getAllReservationsWithRoomsAndUsers(page - 1, this.pageSize).subscribe({
      next: (response: any) => {
        console.log('response', response);
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

  toggleOptions(event: Event, room: any) {
    event.stopPropagation(); // Prevent event from propagating to parent elements
    this.selectedReservation = this.selectedReservation === room ? null : room;
    this.showOptions = this.selectedReservation === room ? !this.showOptions : true;
  }

  editRsvp(rsvp: Reservation) {
    console.log('Edit rsvp', rsvp);
    this.selectedReservation = rsvp;
    this.router.navigate(['/admin/reservation', this.selectedReservation.id, 'edit']);
  }

  viewRsvp(rsvp: Reservation) {
    console.log('View rsvp', rsvp);
    this.selectedReservation = rsvp;
    this.router.navigate(['/admin/reservation', this.selectedReservation?.id, 'view']);
  }

  onDeleteConfirmed(): void {  
    if (this.selectedReservation) {
      this.roomService.deleteRoom(this.selectedReservation.id).subscribe({
        next: () => {
          console.log('Room deleted successful!');
          this.loadReservations(this.currentPage);
          this.toastService.showToast('Room deleted successful!', 'success');
        },
        error: (err) => {
          console.error('Error deleting room:', err);
          this.toastService.showToast('Error deleting room: ' + err, 'error');
        }
      });
    }
    this.showDeleteModal = false;
  }

  deleteRsvp(rsvp: Reservation): void {
    this.selectedReservation = rsvp;
    this.showDeleteModal = true;
    this.showOptions = false;
  }

  onCancelDelete() {
    this.showDeleteModal = false;
  }

  exportData(): void{
    this.reservationService.exportData().subscribe({
      next: () => {
        console.log('export data successful!');
        this.loadReservations(this.currentPage);
        this.toastService.showToast('Data exported successful!', 'success');
      },
      error: (err) => {
        console.error('Error exporting room:', err);
        this.toastService.showToast('Error exporting reservation data', 'error');
      }
    })
  }


}
