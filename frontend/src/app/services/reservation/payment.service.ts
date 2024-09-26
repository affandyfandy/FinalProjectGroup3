import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Payment } from '../../model/payment.model';
import { AppConstants } from "../../config/app.constants";
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = `${AppConstants.BASE_API_V1_URL}/payments`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  createPayment(paymentData: Payment, reservationId: string): Observable<Payment> {
    const headers = new HttpHeaders({
      'Logged-User': this.authService.getUserInformation()[1].value
    });

    return this.http.post<Payment>(`${this.apiUrl}/reservation/${reservationId}`, paymentData, { headers });
  }
}
