import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Payment } from '../../model/payment.model';
import { AppConstants } from "../../config/app.constants";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = `${AppConstants.BASE_API_V1_URL}/payments`;

  constructor(private http: HttpClient) {}

  createPayment(payment: Payment, reservationId: string): Observable<Payment> {
    return this.http.post<Payment>(`${this.apiUrl}/reservation/${reservationId}`, payment);
  }
}
