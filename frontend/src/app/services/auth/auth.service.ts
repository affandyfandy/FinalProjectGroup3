import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AppConstants } from '../../config/app.constants';
import { LoginResponse } from '../../model/login-response.model';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { jwtDecode } from 'jwt-decode';
import { UserService } from '../user.service';
import { User } from '../../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authApiUrl = `${AppConstants.BASE_API_V1_URL}/auth`;
  private tokenName = 'token';

  constructor(
    private http: HttpClient, 
    private router: Router, 
    private cookieService: CookieService, 
    private userService: UserService
  ) { }

  login(username: string, password: string, remember: boolean): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = { username, password };

    return this.http.post<LoginResponse>(`${this.authApiUrl}/login`, body, { headers }).pipe(
      tap(response => {
        if (response && response.token) {
          if (remember) {
            this.storeTokenInCookie(response.token);
          } else {
            this.storeTokenInCookie(response.token);
            // this.storeTokenInSession(response.token);
          }
          this.router.navigate(['/']);
        }
      })
    );
  }

  register(user: User): void {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = { ...user };

    console.log('Registering user', body);

    this.http.post<LoginResponse>(`${this.authApiUrl}/register`, body, { headers }).subscribe(
      response => {
        if (response && response.token) {
          this.storeTokenInCookie(response.token);
          this.router.navigate(['/']);

          window.location.reload();
        }
      }
    );
  }

  forgotPassword(email: string, password: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = { password };

    return this.http.put(`${this.authApiUrl}/${email}/forgot-password`, body, { headers }).pipe(
      tap(() => {
        window.location.reload();
      })
    );
  }

  storeTokenInCookie(token: string): void {
    const expirationTime = new Date();
    expirationTime.setHours(expirationTime.getHours() + 10);

    this.cookieService.set(this.tokenName, token, expirationTime, '/');
  }

  storeTokenInSession(token: string): void {
    sessionStorage.setItem(this.tokenName, token);
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
    sessionStorage.removeItem(this.tokenName);
    this.router.navigate(['/']);
  }

  isAdmin(): boolean {
    if (this.checkCredentials()) {
      const userPayload = this.getUserInformation();
      return userPayload[2].value === 'ADMIN';
    } else {
      return false;
    }
  }
}
