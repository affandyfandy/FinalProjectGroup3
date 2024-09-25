import { Injectable } from "@angular/core";
import { AppConstants } from "../config/app.constants";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Room, RoomResponse } from "../model/room.model";
import { AuthService } from "./auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class RoomService {

    private baseUrl = `${AppConstants.BASE_API_V1_URL}/room`;

    constructor(private http: HttpClient, private authService: AuthService){}

    getAvailableRooms(
        checkIn: string,
        checkOut: string,
        capacity: number = 1,
        page: number,
        size: number
    ): Observable<RoomResponse> {
        const params = new HttpParams()
        .set('checkIn', checkIn)
        .set('checkOut', checkOut)
        .set('capacity', capacity.toString())
        .set('pageNo', page.toString())
        .set('pageSize', size.toString());
        return this.http.get<RoomResponse>(`${this.baseUrl}/available`, { params });
    }

    getAllRooms(
        pageNo: number,
        pageSize: number, 
        sortBy: string,
        sortOrder: string
    ): Observable<RoomResponse> {
        const params = new HttpParams()
        .set('pageNo', pageNo.toString())
        .set('pageSize', pageSize.toString())
        .set('sortBy', sortBy.toString())
        .set('sortOrder', sortOrder.toString());
        return this.http.get<RoomResponse>(this.baseUrl, { params });
    }

    getAllActiveRooms(
        pageNo: number,
        pageSize: number,
    ): Observable<RoomResponse> {
        const params = new HttpParams()
        .set('pageNo', pageNo.toString())
        .set('pageSize', pageSize.toString());
        return this.http.get<RoomResponse>(`${this.baseUrl}/active`, { params });
    }

    getRoomById(id: string): Observable<any> {
        return this.http.get<Room>(`${this.baseUrl}/${id}`);
    }

    deleteRoom(id: string): Observable<any> {
        return this.http.delete<Room>(`${this.baseUrl}/${id}`);
    }

    activateRoom(id: string): Observable<Room> {
        const headers = new HttpHeaders({
            'Logged-User': this.authService.getUserInformation()[1].value
        });
        return this.http.put<Room>(`${this.baseUrl}/${id}/activate`, { headers });
    }

    deactivateRoom(id: string): Observable<Room> {
        const headers = new HttpHeaders({
            'Logged-User': this.authService.getUserInformation()[1].value
        });
        return this.http.put<Room>(`${this.baseUrl}/${id}/deactivate`, { headers });
    }

    editRoomData(id: string, roomData: Room): Observable<Room>{
        const headers = new HttpHeaders({
            'Logged-User': this.authService.getUserInformation()[1].value
        });
        return this.http.put<Room>(`${this.baseUrl}/${id}`, roomData, { headers });
    }

    createRoom(roomData: FormData): Observable<Room>{
        console.log("roomData");
        console.log(roomData);
        const headers = new HttpHeaders({
            'Logged-User': this.authService.getUserInformation()[1].value
        });
        return this.http.post<Room>(`${this.baseUrl}/create`, roomData, { headers });
    }

    importRoom(file: File): Observable<any> {
        const formData = new FormData();
        formData.append('file', file);
        return this.http.post<any>(`${this.baseUrl}/import`, formData, {
          observe: 'events',
          reportProgress: true,
          responseType: 'text' as 'json'
        });
    }

    encodePhoto(file: File): Promise<string> {
        return new Promise((resolve, reject) => {
          const reader = new FileReader();
          reader.onloadend = () => {
            resolve(reader.result as string); 
          };
          reader.onerror = reject; 
          reader.readAsDataURL(file); 
        });
    }
    
    filterRooms(
        pageNo: number = 0,
        pageSize: number = 10,
        status?: string,
        facility?: string,
        capacity?: number,
        roomType?: string,
        lowerLimitPrice?: number
      ): Observable<RoomResponse>{
        let params = new HttpParams()
          .set('pageNo', pageNo.toString())
          .set('pageSize', pageSize.toString());
        if (status) {
          params = params.set('status', status);
        }
        if (facility) {
          params = params.set('facility', facility);
        }
        if (capacity !== undefined) {
          params = params.set('capacity', capacity.toString());
        }
        if (roomType) {
          params = params.set('roomType', roomType);
        }
        if (lowerLimitPrice !== undefined) {
          params = params.set('lowerLimitPrice', lowerLimitPrice.toString());
        }
    
        return this.http.get<RoomResponse>(`${this.baseUrl}/search`, { params });
      }
}