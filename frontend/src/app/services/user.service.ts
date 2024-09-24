import { Injectable } from '@angular/core';
import { AppConstants } from '../config/app.constants';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { User } from '../model/user.model';
import { Page } from '../model/page.model';
import { AuthService } from './auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private authApiUrl = `${AppConstants.BASE_API_V1_URL}/users`;

  constructor(private htpp: HttpClient, private authService: AuthService) { }

  getUsers(page: number = 0, size: number = 10, sort: string = ''): Observable<Page<User>> {
    let params = new HttpParams().set('page', page).set('size', size).set('sort', sort); 
    return this.htpp.get<Page<User>>(this.authApiUrl, { params });
  }

  getUserById(id: string): Observable<any> {
    return this.htpp.get<User>(`${this.authApiUrl}/${id}`);
  }

  createUser(user: User): Observable<any> {
    const headers = new HttpHeaders({
      'Logged-User': this.authService.getUserInformation()[1].value
    });
    return this.htpp.post<User>(this.authApiUrl, user, { headers });
  }

  updateUser(user: User): Observable<User> {
    const headers = new HttpHeaders({
      'Logged-User': this.authService.getUserInformation()[1].value
    });
    return this.htpp.put<User>(`${this.authApiUrl}/${user.email}`, user, { headers });
  }

  changeUserPassword(id: string, password: string): Observable<any> {
    if (this.authService.isAdmin()) {
      return this.htpp.put<User>(`${this.authApiUrl}/${id}/password`, password, {
        headers: new HttpHeaders({
          'Logged-User': this.authService.getUserInformation()[1].value
        })
      });
    }
    return this.htpp.put<User>(`${this.authApiUrl}/${id}/password`, password);
  }

  toggleUserStatus(id: string): Observable<any> {
    const headers = new HttpHeaders({
      'Logged-User': this.authService.getUserInformation()[1].value
    });

    return this.htpp.put<User>(`${this.authApiUrl}/${id}/status`, null, { headers });
  }

  deleteUser(id: string): Observable<any> {
    const headers = new HttpHeaders({
      'Logged-User': this.authService.getUserInformation()[1].value
    });
    return this.htpp.delete<User>(`${this.authApiUrl}/${id}`, { headers });
  }

  getLoggedUser(): Observable<User | null> {
    if (this.authService.checkCredentials()) {
      let userPayload = this.authService.getUserInformation();
      return this.getUserById(userPayload[1].value);
    }

    return of(null);
  }
}
