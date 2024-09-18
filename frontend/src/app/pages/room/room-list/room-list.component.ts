import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RoomFormComponent } from '../room-form/room-form.component';
import { Room } from '../../../models/room.model';
import { RoomService } from '../../../services/room.service';

@Component({
  selector: 'app-room-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RoomFormComponent
  ],
  templateUrl: './room-list.component.html'
})
export class RoomListComponent implements OnInit{
  rooms: Room[] = [];

  constructor(
    private roomService: RoomService
  ){}

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }


}
