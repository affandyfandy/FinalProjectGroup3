import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Room, RoomType, Status } from '../../../../model/room.model';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomService } from '../../../../services/room.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';
import { RouterConfig } from '../../../../config/route.constants';

@Component({
  selector: 'app-room-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent,
    FormsModule
  ],
  templateUrl: './room-form.component.html',
  providers: [
    provideIcons({ heroChevronLeft})
  ]
})
export class RoomFormComponent implements OnInit {
  room: Room | null = null;
  roomTypes: RoomType = RoomType.DOUBLE;
  roomStatus: Status = Status.ACTIVE;
  roomId: string | null = null;

  checkIn: string = '';
  checkOut: string = '';

  queryParam: any = {};

  constructor(
    private roomService: RoomService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.roomId = this.activatedRoute.snapshot.paramMap.get('id');
    if (this.roomId) {
      this.roomService.getRoomById(this.roomId).subscribe((room: Room) => {
        this.room = room;
      });
    }

    this.activatedRoute.queryParams.subscribe(params => {
      this.checkIn = params['checkIn'] !== undefined ? params['checkIn'] : '';
      this.checkOut = params['checkOut'] !== undefined ? params['checkOut'] : '';
    });
  }

  goBack(): void {
    this.router.navigate(['/rooms']);
  }

  bookRoom() {
    if (this.roomId) {
        this.queryParam['roomId'] = this.roomId;
        this.queryParam['checkIn'] = this.checkIn;
        this.queryParam['checkOut'] = this.checkOut;

        this.router.navigate([RouterConfig.RESERVATION.link, 'create'], {
          queryParams: this.queryParam
        });
    }
  }
}
