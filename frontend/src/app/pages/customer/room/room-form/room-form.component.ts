import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Room, RoomType, Status } from '../../../../model/room.model';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomService } from '../../../../services/room.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';

@Component({
  selector: 'app-room-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent
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

  constructor(
    private roomService: RoomService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.roomId = this.route.snapshot.paramMap.get('id');
    if (this.roomId) {
      this.roomService.getRoomById(this.roomId).subscribe((room: Room) => {
        this.room = room;
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/rooms']);
  }

  bookRoom() {
    if (this.roomId) {
        this.router.navigate(['/reservation', this.roomId]);
    }
  }
}
