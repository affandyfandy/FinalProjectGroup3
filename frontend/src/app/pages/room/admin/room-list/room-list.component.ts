import { Component } from '@angular/core';
import { Room, RoomResponse } from '../../../../model/room.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { AuthService } from '../../../../services/auth/auth.service';
import { RoomService } from '../../../../services/room.service';
import { RoomFormComponent } from '../room-form/room-form.component';
import { heroEllipsisVertical } from '@ng-icons/heroicons/outline';
import { heroUserSolid, heroStarSolid, heroAdjustmentsHorizontalSolid } from '@ng-icons/heroicons/solid';

@Component({
  selector: 'app-room-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RoomFormComponent,
    NgIconComponent
  ],
  templateUrl: './room-list.component.html',
  providers: [
    provideIcons({ heroEllipsisVertical, heroUserSolid, heroStarSolid, heroAdjustmentsHorizontalSolid})
  ]
})
export class RoomListComponent {

  rooms: Room[] = [];
  totalElements: number = 0;
  totalPages: number = 0;
  currentPage: number = 1;
  pageSize: number = 10;
  sortColumn: string = 'name';
  sortDirection: string = 'asc';
  error: string = '';
  isAdmin: boolean = false;

  selectedRoom: Room | null = null;
  showOptions: boolean = false;
  isModalVisible: boolean = false;
  action: string = 'add';

  constructor(
    private roomService: RoomService,
    private authService: AuthService,
    private router: Router
  ){}

  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();
    this.loadRooms(this.currentPage);
  }

  loadRooms(currentPage: number) {
    this.roomService.getAllRooms(currentPage-1, this.pageSize).subscribe({
      next: (response: RoomResponse) => {
        console.log("Full response:", response);
        this.rooms = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.currentPage = currentPage;
      },
      error: (err) => {
        this.error = 'Nothing to see..';
        console.error(err);
        // this.toastService.showToast('Failed to load products', 'error');
      }
    });
  }
}
