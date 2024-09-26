import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { forkJoin, map, Observable, switchMap } from 'rxjs';
import { AppConstants } from "../../config/app.constants";
import { Reservation } from '../../model/reservation.model';
import { RoomService } from '../room.service';
import { Room } from '../../model/room.model';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = `${AppConstants.BASE_API_V1_URL}/reservations`;

  constructor(private http: HttpClient, private roomService : RoomService, 
    private authService: AuthService
  ) {}

  getAllReservationsWithRooms(page: number = 0, size: number = 10): Observable<Reservation[]> {
    return this.getAllReservations(page, size).pipe(
      switchMap((reservationsResponse: any) => {
          const reservations: Reservation[] = reservationsResponse.content;

          const roomObservables = reservations.map((reservation: Reservation) => 
              this.roomService.getRoomById(reservation.roomId).pipe(
                  map((room: Room) => ({
                      ...reservation,
                      room: room
                  }))
              )
          );

          return forkJoin(roomObservables);
      }),
      map((reservationsWithRooms: Reservation[]) => {
          return reservationsWithRooms;
      })
  );
  }

  getAllReservations(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(this.apiUrl, { params });
  }

  getReservationById(id: string): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/${id}`);
  }

  createReservation(reservation: Reservation): Observable<Reservation> {
    const headers = new HttpHeaders({
      'Logged-User': this.authService.getUserInformation()[1].value
  });
    return this.http.post<Reservation>(this.apiUrl, reservation, { headers });
  }

  updateReservation(id: string, reservation: Reservation): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}/${id}`, reservation);
  }

  deleteReservation(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  searchReservations(params: any): Observable<Reservation[]> {
    const httpParams = new HttpParams({ fromObject: params });
    return this.http.get<Reservation[]>(`${this.apiUrl}/search`, { params: httpParams });
  }
}

