import { Injectable } from "@angular/core";
import { AppConstants } from "../config/app.constants";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { RoomResponse } from "../models/room.model";

const baseUrl = AppConstants.BASE_API_V1_URL + '/product';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
    constructor(private http: HttpClient){}

    getAllRooms(
        pageNo: number,
        pageSize: number, 
    ): Observable<RoomResponse> {
        const params = new HttpParams()
        .set('pageNo', pageNo.toString())
        .set('pageSize', pageSize.toString());

        return this.http.get<RoomResponse>(baseUrl, { params });
    }
}