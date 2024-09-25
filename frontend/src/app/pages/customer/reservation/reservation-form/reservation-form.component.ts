import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Room } from '../../../../model/room.model';
import { UserService } from '../../../../services/user.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { RoomService } from '../../../../services/room.service';
import { User } from '../../../../model/user.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reservation-form',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './reservation-form.component.html',
  styleUrl: './reservation-form.component.scss'
})
export class ReservationFormComponent implements OnInit {

  user: User | null = null;
  room: Room | null = null;
  
  queryParam: any = {};
  
  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    private roomService: RoomService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.queryParam = params;
    });
    this.loadData();
  }

  loadData(): void {
    if (this.authService.checkCredentials()) {
      this.userService.getUserById(this.authService.getUserInformation()[1].value).subscribe((user: User) => {
        this.user = user;
      });

      this.roomService.getRoomById(this.queryParam.roomId).subscribe((room: Room) => {
        this.room = room;
      });
    }
  }
  
}
