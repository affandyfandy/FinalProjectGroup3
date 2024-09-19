import { Injectable } from "@angular/core";
import { AppConstants } from "../config/app.constants";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Room, RoomResponse } from "../model/room.model";

@Injectable({
  providedIn: 'root'
})
export class RoomService {

    private baseUrl = `${AppConstants.BASE_API_V1_URL}/room`;

    constructor(private http: HttpClient){}

    getAllRooms(
        pageNo: number,
        pageSize: number, 
    ): Observable<RoomResponse> {
        const params = new HttpParams()
        .set('pageNo', pageNo.toString())
        .set('pageSize', pageSize.toString());
        return this.http.get<RoomResponse>(this.baseUrl, { params });
    }

    getRoomById(id: string): Observable<any> {
        return this.http.get<Room>(`${this.baseUrl}/${id}`);
    }

    activateRoom(id: string): Observable<Room> {
        return this.http.post<Room>(`${this.baseUrl}/${id}/activate`, {});
    }

    deactivateRoom(id: string): Observable<Room> {
        return this.http.post<Room>(`${this.baseUrl}/${id}/deactivate`, {});
    }

    // when create new room, the updatedby will be assigned
}