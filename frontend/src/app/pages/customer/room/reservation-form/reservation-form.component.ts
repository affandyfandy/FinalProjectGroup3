import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomService } from '../../../../services/room.service';
import { ReservationService } from '../../../../services/reservation/reservation.service';
import { Room } from '../../../../model/room.model';
import { Reservation } from '../../../../model/reservation.model';

@Component({
  selector: 'app-reservation-form',
  templateUrl: './reservation-form.component.html'
})
export class ReservationFormComponent implements OnInit {
  room: Room | null = null;

  reservation: Reservation = {
    id: '',
    reservationDate: new Date().toISOString().split('T')[0],
    checkInDate: '',
    checkOutDate: '',
    status: 'Confirmed',
    userId: '',
    roomId: '',
    room: {} as Room,
    amount: 0
  };

  constructor(
    private route: ActivatedRoute,
    private roomService: RoomService,
    private reservationService: ReservationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const roomId = this.route.snapshot.paramMap.get('id');
    if (roomId) {
      this.roomService.getRoomById(roomId).subscribe(room => {
        this.room = room;
        this.reservation.roomId = room.id;
        this.reservation.amount = room.price;
      });
    }
  }

  submitReservation(): void {
    this.reservationService.createReservation(this.reservation).subscribe(reservation => {
      this.router.navigate(['/payment', reservation.id]);
    });
  }
}
