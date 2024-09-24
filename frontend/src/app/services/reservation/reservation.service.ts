import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from "../../config/app.constants";
import { Reservation } from '../../model/reservation.model';
import { RoomService } from '../room.service';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = `${AppConstants.BASE_API_V1_URL}/reservations`;

  constructor(private http: HttpClient, private roomService : RoomService) {}

  getAllReservations(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(this.apiUrl, { params });
  }

  getReservationById(id: string): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/${id}`);
  }

  createReservation(reservation: Reservation): Observable<Reservation> {
    return this.http.post<Reservation>(this.apiUrl, reservation);
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

