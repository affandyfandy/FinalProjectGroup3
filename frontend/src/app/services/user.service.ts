import { Injectable } from '@angular/core';
import { AppConstants } from '../config/app.constants';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';
import { Page } from '../model/page.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private authApiUrl = `${AppConstants.BASE_API_V1_URL}/users`;

  constructor(private htpp: HttpClient) { }

  getUsers(page: number = 0, size: number = 10): Observable<Page<User>> {
    let params = new HttpParams().set('page', page).set('size', size); 
    return this.htpp.get<Page<User>>(this.authApiUrl, { params });
  }

  getUserById(id: string): Observable<any> {
    return this.htpp.get<User>(`${this.authApiUrl}/${id}`);
  }

  createUser(user: User): Observable<any> {
    return this.htpp.post<User>(this.authApiUrl, user);
  }

  updateUser(user: User): Observable<User> {
    return this.htpp.put<User>(`${this.authApiUrl}/${user.email}`, user);
  }

  changeUserPassword(id: string, password: string): Observable<any> {
    return this.htpp.put<User>(`${this.authApiUrl}/${id}/password`, password);
  }

  toggleUserStatus(id: string): Observable<any> {
    return this.htpp.put<User>(`${this.authApiUrl}/${id}/status`, null);
  }

  deleteUser(id: string): Observable<any> {
    return this.htpp.delete<User>(`${this.authApiUrl}/${id}`);
  }
}
