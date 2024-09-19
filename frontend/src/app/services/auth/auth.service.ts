import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AppConstants } from '../../config/app.constants';
import { LoginResponse } from '../../model/login-response.model';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authApiUrl = `${AppConstants.BASE_API_V1_URL}/auth`;
  private tokenName = 'token';

  constructor(private http: HttpClient, private router: Router, private cookieService: CookieService) { }

  login(username: string, password: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = { username, password };

    return this.http.post<LoginResponse>(`${this.authApiUrl}/login`, body, { headers }).pipe(
      tap(response => {
        if (response && response.token) {
          this.storeTokenInCookie(response.token);
          this.router.navigate(['/']);
        }
      })
    );
  }

  storeTokenInCookie(token: string): void {
    const expirationTime = new Date();
    expirationTime.setHours(expirationTime.getHours() + 10);

    this.cookieService.set(this.tokenName, token, expirationTime, '/');
  }

  getToken(): string | null {
    return this.cookieService.get(this.tokenName);
  }

  getUserInformation(): any {
    const token = this.getToken();
    if (token) {
      const decodedToken = jwtDecode(token);
      return Object.entries(decodedToken).map(([key, value]) => ({ key, value }));
    }
  }

  checkCredentials(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    this.cookieService.delete(this.tokenName);
    this.router.navigate(['/']);
  }
}
