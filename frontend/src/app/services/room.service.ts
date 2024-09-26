import { Injectable } from "@angular/core";
import { AppConstants } from "../config/app.constants";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { catchError, map, Observable, of } from "rxjs";
import { Room, RoomResponse } from "../model/room.model";
import { AuthService } from "./auth/auth.service";
import { DomSanitizer, SafeUrl } from "@angular/platform-browser";
import { Page } from "../model/page.model";

@Injectable({
  providedIn: 'root'
})
export class RoomService {

    private baseUrl = `${AppConstants.BASE_API_V1_URL}/room`;

    constructor(private http: HttpClient, private authService: AuthService, private sanitizer: DomSanitizer){}

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
    
    uploadRoomPhoto(id: string, file: File): Observable<any> {
        const formData: FormData = new FormData();
        formData.append('file', file);
        return this.http.post(`${this.baseUrl}/${id}/photo`, formData);
    }

    getRoomPhoto(photo: string): Observable<Blob> {
        return this.http.get(`${this.baseUrl}${photo}`, { responseType: 'blob' });
    }

    fetchRoomPhoto(photo: string): Observable<SafeUrl | null> {
        return this.getRoomPhoto(photo).pipe(
            map((response: Blob) => {
                const objectUrl = URL.createObjectURL(response);
                const safeUrl = this.sanitizer.bypassSecurityTrustUrl(objectUrl);
                return safeUrl;
            }),
            catchError((error) => {
                return of(null);
            })
        );
    }
    
    getAllRoomsAsList(): Observable<any> {
        return this.http.get(`${this.baseUrl}/list/all`);
    }

    filterRooms(
        pageNo?: number,
        pageSize?: number,
        roomNumber?: string,
        capacity?: number,
        roomType?: string,
        price?: string,
        status?: string,
      ): Observable<RoomResponse>{
        let params = new HttpParams()
        if (pageNo){
          params = params.set('pageNo', pageNo);
        }
        if (pageSize){
           params = params.set('pageSize', pageSize);
        }
        if (status) {
          params = params.set('status', status);
        }
        if (roomNumber) {
          params = params.set('roomNumber', roomNumber);
        }
        if (capacity !== undefined) {
          params = params.set('capacity', capacity.toString());
        }
        if (roomType) {
          params = params.set('roomType', roomType);
        }
        if (price !== undefined) {
          params = params.set('lowerLimitPrice', price.toString());
        }
        console.log("params in service " + params);
    
        return this.http.get<RoomResponse>(`${this.baseUrl}/search`, { params });
      }

    filterRoomTypes(roomType: string, pageNo: number = 0, pageSize: number = 10): Observable<any> {
    let params = new HttpParams()
        .set('roomType', roomType)
        .set('pageNo', pageNo.toString())
        .set('pageSize', pageSize.toString());

    console.log('Filtering rooms by type:', roomType);
    return this.http.get<any>(this.baseUrl + '/search', { params });
    }
}